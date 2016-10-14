package ua.nure.tarasenko.summary4.db.derby;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.db.GenericDao;
import ua.nure.tarasenko.summary4.entity.Account;
import ua.nure.tarasenko.summary4.util.Constants;

/**
 * Provides methods for creating, reading, updating, deleting objects of class
 * Account in database.
 * 
 * @author Tarasenko
 */
public class DerbyAccountDao implements GenericDao<Account> {

	private static final Logger LOG = Logger.getLogger(DerbyAccountDao.class);

	@Override
	public void create(Account account) throws SQLException {
		LOG.info("Start creating account for client " + account.getClientId());
		String request = "INSERT INTO account (client_id, name, amount) VALUES (?, ?, ?)";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement;
		statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		statement.setInt(1, account.getClientId());
		statement.setString(2, account.getName());
		statement.setBigDecimal(3, account.getAmount());
		statement.executeUpdate();
		conn.close();
		LOG.info("End creating account for client " + account.getClientId());
	}

	@Override
	public Account read(long id) throws SQLException {
		LOG.info("Start reading account by id " + id + " from DB");
		String request = "SELECT * FROM ACCOUNT_TRANSLATE JOIN ACCOUNT ON ACCOUNT_TRANSLATE.ACCOUNT_ID = ACCOUNT.ID WHERE ACCOUNT_TRANSLATE.LANGUAGE_ID = ? AND ACCOUNT_TRANSLATE.ACCOUNT_ID = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement;
		Account account = null;
		statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		statement.setInt(1, Constants.LANGUAGE_ID);
		statement.setLong(2, id);
		ResultSet rs = statement.executeQuery();
		rs.beforeFirst();
		rs.first();
		account = parseAccount(rs);
		conn.close();
		LOG.info("End reading account " + id + " from DB");
		return account;
	}

	/**
	 * Reads accounts from database by client id.
	 * 
	 * @param id
	 *            Client id.
	 * @return Collections with accounts.
	 * @throws SQLException
	 *             If there are errors during interaction with database.
	 */
	public List<Account> readByClientId(int id) throws SQLException {
		LOG.info("Start reading account by client id " + id + " from DB");
		String request = "SELECT * FROM ACCOUNT_TRANSLATE JOIN ACCOUNT ON ACCOUNT_TRANSLATE.ACCOUNT_ID = ACCOUNT.ID WHERE ACCOUNT_TRANSLATE.LANGUAGE_ID = ? AND ACCOUNT.CLIENT_ID = ? ORDER BY "
				+ Constants.ACCOUNT_ORDER.name();
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		List<Account> accounts = new ArrayList<>();
		Account account = null;
		PreparedStatement statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		statement.setInt(1, Constants.LANGUAGE_ID);
		statement.setInt(2, id);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			account = parseAccount(rs);
			accounts.add(account);
		}
		conn.close();
		LOG.info("End reading account " + id + " from DB");
		return accounts;
	}

	@Override
	public void update(Account account) throws SQLException {
		LOG.info("Start updating account for client " + account.getClientId());
		String request = "UPDATE account SET client_id = ?, amount = ? WHERE id = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement;
		statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		statement.setInt(1, account.getClientId());
		statement.setBigDecimal(2, account.getAmount());
		statement.setLong(3, account.getAccountId());
		statement.executeUpdate();
		Map<Integer, String> names = account.getNames();
		String requestTranslate = "UPDATE account_translate SET name = ? WHERE account_id = ? AND language_id = ?";
		for (Integer key : names.keySet()) {
			PreparedStatement statementTranslate;
			statementTranslate = conn.prepareStatement(requestTranslate, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			statementTranslate.setString(1, names.get(key));
			statementTranslate.setLong(2, account.getAccountId());
			statementTranslate.setInt(3, key);
			statementTranslate.executeUpdate();
		}
		conn.close();
		LOG.info("End updating account for client " + account.getClientId());
	}

	@Override
	public List<Account> readAll() throws SQLException {
		LOG.info("Start reading all accounts");
		String request = "SELECT * FROM ACCOUNT_TRANSLATE JOIN ACCOUNT ON ACCOUNT_TRANSLATE.ACCOUNT_ID = ACCOUNT.ID WHERE ACCOUNT_TRANSLATE.LANGUAGE_ID = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		List<Account> accounts = new ArrayList<>();
		Account account = null;
		PreparedStatement statement;
		statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		statement.setInt(1, Constants.LANGUAGE_ID);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			account = parseAccount(rs);
			accounts.add(account);
		}
		conn.close();
		LOG.info("End reading all accounts");
		return accounts;
	}

	@Override
	public void delete(int id) throws SQLException {
		LOG.info("Start deleting account with id " + id);
		String request = "DELETE FROM account WHERE account_id = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement;
		statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		statement.setInt(1, id);
		statement.executeUpdate();
		conn.close();
		LOG.info("End deleting account with id " + id);
	}

	private Account parseAccount(ResultSet rs) throws SQLException {
		LOG.info("Start parsing account");
		Account account = new Account();
		try {
			account.setAccountId(rs.getLong("id"));
			account.setClientId(rs.getInt("client_id"));
			account.setName(rs.getInt("language_id"), rs.getString("name"));
			account.setAmount(rs.getBigDecimal("amount"));
			LOG.info("Account was succesfully parsed");
		} catch (SQLException e) {
			LOG.error("Cannot parse account from ResultSet");
			throw new SQLException();
		}
		LOG.info("End parsing account");
		return account;
	}

	public BigDecimal calculateSum(long accountId, String request) throws SQLException {
		BigDecimal sum = null;
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement stmt = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		stmt.setLong(1, accountId);
		ResultSet rs = stmt.executeQuery();
		rs.beforeFirst();
		rs.first();
		sum = rs.getBigDecimal(1);
		return sum;
	}
}