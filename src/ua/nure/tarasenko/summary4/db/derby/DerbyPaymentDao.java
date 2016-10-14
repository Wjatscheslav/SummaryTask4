package ua.nure.tarasenko.summary4.db.derby;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.db.GenericDao;
import ua.nure.tarasenko.summary4.entity.Payment;
import ua.nure.tarasenko.summary4.util.Constants;

/**
 * Provides methods for creating, reading, updating, deleting objects of class
 * Payment in database.
 * 
 * @author Tarasenko
 */
public class DerbyPaymentDao implements GenericDao<Payment> {

	private static final Logger LOG = Logger.getLogger(DerbyPaymentDao.class);

	@Override
	public void create(Payment payment) throws SQLException {
		LOG.info("Start creating payment for client from account " + payment.getSendAccount() + " to "
				+ payment.getReceiveAccount());
		String request = "INSERT INTO payment (operation_date, send_account, receive_account, amount) VALUES (?, ?, ?, ?)";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		statement.setDate(1, payment.getOperationDate());
		statement.setLong(2, payment.getSendAccount());
		statement.setLong(3, payment.getReceiveAccount());
		statement.setBigDecimal(4, payment.getAmount());
		statement.executeUpdate();
		conn.close();
		LOG.info("End creating payment for client from account " + payment.getSendAccount() + " to "
				+ payment.getReceiveAccount());
	}

	@Override
	public Payment read(long id) throws SQLException {
		LOG.info("Start reading payment by id " + id);
		String request = "SELECT * FROM payment WHERE payment_id = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		statement.setLong(1, id);
		ResultSet rs = statement.executeQuery();
		rs.beforeFirst();
		rs.first();
		Payment payment = parsePayment(rs);
		conn.close();
		LOG.info("End reading payment by id " + id);
		return payment;
	}

	public List<Payment> readByAccountId(long id) throws SQLException {
		LOG.info("Start reading payment by account id " + id);
		String request = "SELECT * FROM PAYMENT WHERE SEND_ACCOUNT = ? OR RECEIVE_ACCOUNT = ? ORDER BY "
				+ Constants.PAYMENT_ORDER.name() + " " + Constants.ORDER.name();
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		List<Payment> payments = new ArrayList<>();
		PreparedStatement statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		statement.setLong(1, id);
		statement.setLong(2, id);
		Payment payment = null;
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			payment = parsePayment(rs);
			payments.add(payment);
		}
		conn.close();
		LOG.info("End reading payment");
		return payments;
	}

	@Override
	public void update(Payment payment) throws SQLException {
		LOG.info("Start updating payment by id " + payment.getPaymentId());
		String request = "UPDATE payment SET operation_date = ?, send_account = ?, receive_account = ?, amount = ? WHERE payment_id = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		statement.setDate(1, payment.getOperationDate());
		statement.setLong(2, payment.getSendAccount());
		statement.setLong(3, payment.getReceiveAccount());
		statement.executeUpdate();
		conn.close();
		LOG.info("End updating payment by id " + payment.getPaymentId());
	}

	@Override
	public List<Payment> readAll() throws SQLException {
		LOG.info("Start reading all payments");
		String request = "SELECT * FROM payment";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		List<Payment> payments = new ArrayList<>();
		Payment payment = null;
		PreparedStatement statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			payment = parsePayment(rs);
			payments.add(payment);
		}
		conn.close();
		LOG.info("End reading all payments");
		return payments;
	}

	@Override
	public void delete(int id) throws SQLException {
		LOG.info("Start deleting payment with id " + id);
		String request = "DELETE FROM payment WHERE payment_id = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		statement.setInt(1, id);
		statement.executeUpdate();
		conn.close();
		LOG.info("End deleting payment with id " + id);
	}

	/**
	 * Parses object of class Payment from ResultSet.
	 * 
	 * @param rs
	 *            Result set.
	 * @return Object of class Payment.
	 * @throws SQLException
	 *             If there are errors during interaction with database.
	 */
	private Payment parsePayment(ResultSet rs) throws SQLException {
		LOG.info("Start parsing autorization");
		Payment payment = new Payment();
		try {
			payment.setPaymentId(rs.getLong("payment_id"));
			payment.setOperationDate(rs.getDate("operation_date"));
			payment.setSendAccount(rs.getLong("send_account"));
			payment.setReceiveAccount(rs.getLong("receive_account"));
			payment.setAmount(rs.getBigDecimal("amount"));
			LOG.info("Payment was succesfully parsed");
		} catch (SQLException e) {
			LOG.error("Cannot parse payment from account " + rs.getString("send_account") + " to account "
					+ rs.getString("receive_account"));
			throw new SQLException();
		}
		LOG.info("End parsing payment");
		return payment;
	}
}
