package ua.nure.tarasenko.summary4.db.derby;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.db.DaoFactory;
import ua.nure.tarasenko.summary4.db.GenericDao;
import ua.nure.tarasenko.summary4.entity.Account;
import ua.nure.tarasenko.summary4.entity.Payment;

public class TransferManager {

	private static final Logger LOG = Logger.getLogger(TransferManager.class);

	public synchronized void doTransfer(Account send, Account receive) throws SQLException {
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		String request = "UPDATE account SET client_id = ?, amount = ? WHERE id = ?";
		try {
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement(request);
			stmt.setInt(1, send.getClientId());
			stmt.setBigDecimal(2, send.getAmount());
			stmt.setLong(3, send.getAccountId());
			stmt.executeUpdate();
			stmt = conn.prepareStatement(request);
			stmt.setInt(1, receive.getClientId());
			stmt.setBigDecimal(2, receive.getAmount());
			stmt.setLong(3, receive.getAccountId());
			stmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {

			}
		}
		conn.close();
	}

	/**
	 * Adds record in table Payment into DB.
	 * 
	 * @param sendAccount
	 *            Send account.
	 * @param receiveAccount
	 *            Receive account.
	 * @param amount
	 *            Amount.
	 */
	public void addTransferToDB(long sendAccount, long receiveAccount, BigDecimal amount) {
		LOG.info("Start adding transfer to history");
		DaoFactory factory = new DerbyDaoFactory();
		Payment payment = new Payment();
		payment.setSendAccount(sendAccount);
		payment.setReceiveAccount(receiveAccount);
		payment.setAmount(amount);
		Calendar cal = Calendar.getInstance();
		long now = cal.getTimeInMillis();
		Date date = new Date(now);
		payment.setOperationDate(date);
		GenericDao<Payment> dao = factory.getPaymentDao();
		try {
			dao.create(payment);
		} catch (SQLException e) {
			LOG.error("Cannot add transfer to history");
		}
		LOG.info("End adding transfer to history");
	}

}
