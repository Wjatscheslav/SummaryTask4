package ua.nure.tarasenko.summary4.db.derby;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.db.GenericDao;
import ua.nure.tarasenko.summary4.entity.CreditCard;

/**
 * Provides methods for creating, reading, updating, deleting objects of class
 * CreditCard in database.
 * 
 * @author Tarasenko
 */
public class DerbyCreditCardDao implements GenericDao<CreditCard> {

	private static final Logger LOG = Logger.getLogger(DerbyCreditCardDao.class);

	@Override
	public void create(CreditCard card) {
		LOG.info("Start creating credit card for client " + card.getClientId());
		String request = "INSERT INTO credit_card (client_id, account_id, pin, locked) VALUES (?, ?, ?, ?)";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		try (PreparedStatement statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY)) {
			conn.setAutoCommit(false);
			statement.setInt(1, card.getClientId());
			statement.setLong(2, card.getAccountId());
			statement.setInt(3, card.getPin());
			statement.setBoolean(4, card.isLocked());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				conn.commit();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		LOG.info("End creating card for client " + card.getClientId());
	}

	@Override
	public CreditCard read(long id) throws SQLException {
		LOG.info("Start reading credit card by id " + id + " from DB");
		String request = "SELECT * FROM credit_card WHERE card_id = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		CreditCard card = null;
		PreparedStatement statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		statement.setLong(1, id);
		ResultSet rs = statement.executeQuery();
		rs.beforeFirst();
		rs.first();
		card = parseCreditCard(rs);
		LOG.info("End reading autorization");
		return card;
	}

	public List<CreditCard> readByClientId(int id) throws SQLException {
		LOG.info("Start reading credit card by client id " + id);
		String request = "SELECT * FROM credit_card WHERE client_id = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		List<CreditCard> cards = new ArrayList<>();
		CreditCard card = null;
		PreparedStatement statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		statement.setInt(1, id);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			card = parseCreditCard(rs);
			cards.add(card);
		}
		conn.close();
		LOG.info("End reading credit card");
		return cards;
	}

	@Override
	public void update(CreditCard card) throws SQLException {
		LOG.info("Start updating credit card for client " + card.getClientId());
		String request = "UPDATE credit_card SET client_id = ?, account_id = ?, pin = ?, locked = ? WHERE card_id = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		statement.setInt(1, card.getClientId());
		statement.setLong(2, card.getAccountId());
		statement.setInt(3, card.getPin());
		statement.setBoolean(4, card.isLocked());
		statement.setString(5, card.getCardId());
		statement.executeUpdate();
		conn.close();
		LOG.info("End updating credit card for client " + card.getClientId());
	}

	@Override
	public List<CreditCard> readAll() throws SQLException {
		LOG.info("Start reading all credit cards");
		String request = "SELECT * FROM credit_card";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		List<CreditCard> cards = new ArrayList<>();
		CreditCard card = null;
		PreparedStatement statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			card = parseCreditCard(rs);
			cards.add(card);
		}
		conn.close();
		LOG.info("End reading all credit cards");
		return cards;
	}

	@Override
	public void delete(int id) throws SQLException {
		LOG.info("Start deleting credit card with id " + id);
		String request = "DELETE FROM credit_card WHERE card_id = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		statement.setInt(1, id);
		statement.executeUpdate();
		conn.close();
		LOG.info("End deleting credit card with id " + id);
	}

	/**
	 * Parses credit card from ResultSet.
	 * 
	 * @param rs
	 *            ResultSet.
	 * @return Credit card.
	 * @throws SQLException
	 *             If there are errors during interaction with database.
	 */
	private CreditCard parseCreditCard(ResultSet rs) {
		LOG.info("Start parsing credit card");
		CreditCard card = new CreditCard();
		try {
			Long cardIdLong = rs.getLong("card_id");
			String cardIdString = String.format("%016d", cardIdLong);
			card.setCardId(cardIdString);
			card.setClientId(rs.getInt("client_id"));
			card.setAccountId(rs.getLong("account_id"));
			card.setPin(rs.getInt("pin"));
			card.setLocked(rs.getBoolean("locked"));
			LOG.info("Credit card was succesfully parsed");
		} catch (SQLException e) {
			LOG.error("Cannot parse credit card from ResultSet");
		}
		return card;
	}

}
