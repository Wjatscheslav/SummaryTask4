package ua.nure.tarasenko.summary4.db.bean;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.db.DaoFactory;
import ua.nure.tarasenko.summary4.db.GenericDao;
import ua.nure.tarasenko.summary4.db.derby.DerbyAccountDao;
import ua.nure.tarasenko.summary4.db.derby.DerbyDaoFactory;
import ua.nure.tarasenko.summary4.entity.Account;

public class AccountBean {

	private static final Logger LOG = Logger.getLogger(AccountBean.class);

	private int clientId;
	private long accountId;
	private List<Account> accounts;
	private Account account;
	
	public void setClientId(int id) {
		this.clientId = id;
		loadAccounts();
	}

	public void setAccountId(int id) {
		this.accountId = id;
		loadAccount();
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public Account getAccount() {
		return account;
	}

	private List<Account> loadAccounts() {
		LOG.info("Start loading accounts for client " + clientId + " from database");
		DaoFactory factory = new DerbyDaoFactory();
		DerbyAccountDao dao = (DerbyAccountDao) factory.getAccountDao();
		try {
			accounts = dao.readByClientId(clientId);
		} catch (SQLException e) {
			LOG.error("Cannot load accounts for client " + clientId);
		}
		LOG.info("End loading accounts for client " + clientId);
		return accounts;
	}

	private Account loadAccount() {
		LOG.info("Start loading account " + accountId + " from database");
		DaoFactory factory = new DerbyDaoFactory();
		GenericDao<Account> dao;
		dao = factory.getAccountDao();
		try {
			account = dao.read(accountId);
		} catch (SQLException e) {
			LOG.error("Cannot load account " + accountId);
		}
		LOG.info("End loading account " + accountId);
		return account;
	}

}
