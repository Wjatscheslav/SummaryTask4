package ua.nure.tarasenko.summary4.db.derby;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.db.GenericDao;
import ua.nure.tarasenko.summary4.entity.Client;
import ua.nure.tarasenko.summary4.entity.Role;
import ua.nure.tarasenko.summary4.util.Constants;

/**
 * Provides methods for creating, reading, updating, deleting objects of class
 * Client in database.
 * 
 * @author Tarasenko
 */
public class DerbyClientDao implements GenericDao<Client> {

	private static final Logger LOG = Logger.getLogger(DerbyClientDao.class);

	@Override
	public void create(Client client) throws SQLException {
		LOG.info("Start creating client with id " + client.getClientId());
		createClientPermanent(client);
		String requestGetId = "SELECT id FROM client WHERE telephone_number = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statementSelect = conn.prepareStatement(requestGetId, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		statementSelect.setString(1, client.getTelephoneNumber());
		ResultSet rs = statementSelect.executeQuery();
		rs.beforeFirst();
		rs.first();
		int clientId = rs.getInt("id");
		statementSelect.close();
		conn.close();
		createClientTranslate(client, clientId);
		LOG.info("End creating client with id " + client.getClientId());
	}

	@Override
	public Client read(long id) throws SQLException {
		LOG.info("Start reading client by card id " + id + " from DB");
		String request = "SELECT CLIENT_TRANSLATE.TRANSLATE_ID, CLIENT_TRANSLATE.LANGUAGE_ID, CLIENT_TRANSLATE.CLIENT_ID, CLIENT_TRANSLATE.NAME, CLIENT_TRANSLATE.SURNAME, CLIENT.ID, CLIENT.BORN_DATE, CLIENT.TELEPHONE_NUMBER, CLIENT.ROLE_ID FROM CREDIT_CARD JOIN CLIENT_TRANSLATE JOIN CLIENT ON CLIENT_TRANSLATE.CLIENT_ID = CLIENT.ID ON CREDIT_CARD.CLIENT_ID = CLIENT.ID WHERE CLIENT_TRANSLATE.LANGUAGE_ID = ? AND CREDIT_CARD.CARD_ID = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		statement.setInt(1, Constants.LANGUAGE_ID);
		statement.setLong(2, id);
		ResultSet rs = statement.executeQuery();
		rs.beforeFirst();
		rs.first();
		Client client = parseClient(rs);
		conn.close();
		LOG.info("End reading client by card id");
		return client;
	}

	/**
	 * Reads client by client id.
	 * 
	 * @param id
	 *            Client id.
	 * @return Client.
	 * @throws SQLException
	 *             If there are errors during interaction with database.
	 */
	public Client read(int id) throws SQLException {
		LOG.info("Start reading client by id " + id + " from DB");
		String request = "SELECT * FROM CLIENT_TRANSLATE JOIN CLIENT ON CLIENT_TRANSLATE.CLIENT_ID = CLIENT.ID WHERE CLIENT_TRANSLATE.LANGUAGE_ID = ? AND CLIENT_TRANSLATE.CLIENT_ID = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		statement.setInt(1, Constants.LANGUAGE_ID);
		statement.setLong(2, id);
		ResultSet rs = statement.executeQuery();
		rs.beforeFirst();
		rs.first();
		Client client = parseClient(rs);
		conn.close();
		LOG.info("End reading client");
		return client;
	}

	@Override
	public void update(Client client) throws SQLException {
		LOG.info("Start updating client with id " + client.getClientId());
		String requestClient = "UPDATE client SET born_date = ?, telephone_number = ?, role_id = ? WHERE id = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement = conn.prepareStatement(requestClient, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		statement.setString(1, client.getBornDate());
		statement.setString(2, client.getTelephoneNumber());
		statement.setInt(3, client.getRole().getId());
		statement.setInt(4, client.getClientId());
		statement.executeUpdate();
		statement.close();
		Map<Integer, String> names = client.getNames();
		Map<Integer, String> surnames = client.getSurnames();
		String requestTranslate = "UPDATE client_translate SET name = ?, surname = ? WHERE client_id = ? AND language_id = ?";
		for (Integer key : names.keySet()) {
			PreparedStatement statementTranslate = conn.prepareStatement(requestTranslate,
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			statementTranslate.setString(1, names.get(key));
			statementTranslate.setString(2, surnames.get(key));
			statementTranslate.setInt(3, client.getClientId());
			statementTranslate.setInt(4, key);
			statementTranslate.executeUpdate();
		}
		conn.close();
		LOG.info("End updating client with id " + client.getClientId());
	}

	@Override
	public List<Client> readAll() throws SQLException {
		LOG.info("Start reading all clients");
		String request = "SELECT * FROM CLIENT_TRANSLATE JOIN CLIENT ON CLIENT_TRANSLATE.CLIENT_ID = CLIENT.ID WHERE CLIENT_TRANSLATE.LANGUAGE_ID = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		List<Client> clients = new ArrayList<>();
		Client client = null;
		PreparedStatement statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		statement.setInt(1, Constants.LANGUAGE_ID);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			client = parseClient(rs);
			clients.add(client);
		}
		conn.close();
		LOG.info("End reading all clients");
		return clients;
	}

	@Override
	public void delete(int id) throws SQLException {
		LOG.info("Start deleting client with id " + id);
		String requestTranslate = "DELETE FROM CLIENT_TRANSLATE WHERE CLIENT_TRANSLATE.CLIENT_ID = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement = conn.prepareStatement(requestTranslate, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		statement.setInt(1, id);
		statement.executeUpdate();
		String requestClient = "DELETE FROM CLIENT WHERE CLIENT.ID = ?";
		statement = conn.prepareStatement(requestClient, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		statement.setInt(1, id);
		statement.executeUpdate();
		conn.close();
		LOG.info("End deleting account with id " + id);
	}

	/**
	 * Parses client from ResultSet.
	 * 
	 * @param rs
	 *            ResultSet.
	 * @return Client
	 * @throws SQLException
	 *             If there are errors during interaction with database.
	 */
	private Client parseClient(ResultSet rs) throws SQLException {
		LOG.info("Start parsing client");
		Client client = new Client();
		try {
			client.setClientId(rs.getInt("id"));
			client.setName(rs.getInt("language_id"), rs.getString("name"));
			client.setSurname(rs.getInt("language_id"), rs.getString("surname"));
			client.setBornDate(rs.getString("born_date"));
			client.setTelephoneNumber(rs.getString("telephone_number"));
			client.setRole(Role.getRoleById(rs.getInt("role_id")));
			LOG.info("Client was succesfully parsed");
		} catch (SQLException e) {
			LOG.error("Cannot parse client for user " + rs.getString("login") + " from ResultSet");
			throw new SQLException();
		}
		LOG.info("End parsing client");
		return client;
	}

	/**
	 * Creates unmodifiable part of client.
	 * 
	 * @param client
	 *            Client.
	 * @throws SQLException
	 *             If there are errors during interaction with database.
	 */
	private void createClientPermanent(Client client) throws SQLException {
		LOG.info("Start creating permanent part for client with id " + client.getClientId());
		String requestClient = "INSERT INTO client (born_date, telephone_number, role_id) VALUES (?, ?, ?)";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement = conn.prepareStatement(requestClient, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		statement.setString(1, client.getBornDate());
		statement.setString(2, client.getTelephoneNumber());
		statement.setInt(3, client.getRole().getId());
		statement.executeUpdate();
		statement.close();
		conn.close();
		LOG.info("End creating permanent part for client with id " + client.getClientId());
	}

	/**
	 * Creates translatable part of client.
	 * 
	 * @param client
	 *            Client.
	 * @param clientId
	 *            Client id.
	 * @throws SQLException
	 *             If there are errors during interaction with database.
	 */
	private void createClientTranslate(Client client, int clientId) throws SQLException {
		LOG.info("Start creating translatable part for client with id " + client.getClientId());
		Map<Integer, String> names = client.getNames();
		Map<Integer, String> surnames = client.getSurnames();
		String requestClientTranslate = "INSERT INTO client_translate (language_id, client_id, name, surname) VALUES (?, ?, ?, ?)";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		for (Integer key : names.keySet()) {
			PreparedStatement statementTranslate = conn.prepareStatement(requestClientTranslate,
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			statementTranslate.setInt(1, key);
			statementTranslate.setInt(2, clientId);
			statementTranslate.setString(3, names.get(key));
			statementTranslate.setString(4, surnames.get(key));
			statementTranslate.executeUpdate();
			LOG.info("End creating translatable part for client with id " + client.getClientId());
		}
	}
}
