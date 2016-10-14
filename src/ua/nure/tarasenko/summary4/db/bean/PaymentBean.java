package ua.nure.tarasenko.summary4.db.bean;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.db.DaoFactory;
import ua.nure.tarasenko.summary4.db.derby.DerbyDaoFactory;
import ua.nure.tarasenko.summary4.db.derby.DerbyPaymentDao;
import ua.nure.tarasenko.summary4.entity.Payment;

public class PaymentBean {

	private static final Logger LOG = Logger.getLogger(AccountBean.class);

	private long accountId;
	private List<Payment> payments;

	public void setAccountId(long accountId)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		this.accountId = accountId;
		loadPayments();
	}

	public List<Payment> getPayments() {
		return payments;
	}

	private List<Payment> loadPayments() {
		LOG.info("Start loading payments for account " + accountId + " from database");
		DaoFactory factory = new DerbyDaoFactory();
		DerbyPaymentDao dao = (DerbyPaymentDao) factory.getPaymentDao();
		try {
			this.payments = dao.readByAccountId(accountId);
		} catch (SQLException e) {
			LOG.error("Cannot load payments for account " + accountId);
		}
		LOG.info("End loading payments for account " + accountId);
		return payments;
	}
}
