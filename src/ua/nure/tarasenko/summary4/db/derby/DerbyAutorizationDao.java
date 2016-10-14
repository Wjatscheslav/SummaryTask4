package ua.nure.tarasenko.summary4.db.derby;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.db.GenericDao;
import ua.nure.tarasenko.summary4.entity.Autorization;

/**
 * Provides methods for creating, reading, updating, deleting objects of class
 * Autorization in database.
 * 
 * @author Tarasenko
 */
public class DerbyAutorizationDao implements GenericDao<Autorization> {

	private static final Logger LOG = Logger.getLogger(DerbyAutorizationDao.class);

	@Override
	public void create(Autorization autorization) throws SQLException {
		LOG.info("Start creating autorization for client " + autorization.getClientId());
		String request = "INSERT INTO autorization (login, password, client_id, locked) VALUES (?, ?, ?, ?)";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement;
		statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		statement.setString(1, autorization.getLogin());
		statement.setString(2, autorization.getPassword());
		statement.setInt(3, autorization.getClientId());
		statement.setBoolean(4, false);
		statement.executeUpdate();
		conn.close();
		LOG.info("End creating autorization for client " + autorization.getClientId());
	}

	@Override
	public Autorization read(long id) throws SQLException {
		LOG.info("Start reading autorization by client id " + id + " from DB");
		String request = "SELECT * FROM autorization WHERE client_id = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement;
		Autorization autorization = null;
		statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		statement.setLong(1, id);
		ResultSet rs = statement.executeQuery();
		rs.beforeFirst();
		rs.first();
		autorization = parseAutorization(rs);
		conn.close();
		LOG.info("End reading autorization");
		return autorization;
	}

	/**
	 * Reads autorization by client login.
	 * 
	 * @param login
	 *            Client login.
	 * @return Autorization.
	 * @throws SQLException
	 *             If there are errors during interaction with database.
	 */
	public Autorization read(String login) throws SQLException {
		LOG.info("Start reading autorization by login " + login + " from DB");
		String request = "SELECT * FROM autorization WHERE login = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement;
		Autorization autorization = null;
		statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		statement.setString(1, login);
		ResultSet rs = statement.executeQuery();
		rs.beforeFirst();
		rs.first();
		autorization = parseAutorization(rs);
		conn.close();
		LOG.info("End reading autorization");
		return autorization;
	}

	@Override
	public void update(Autorization autorization) throws SQLException {
		LOG.info("Start updating autorization for client " + autorization.getClientId());
		String request = "UPDATE autorization SET login = ?, password = ?, client_id = ?, locked = ? WHERE client_id = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement;
		statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		statement.setString(1, autorization.getLogin());
		statement.setString(2, autorization.getPassword());
		statement.setInt(3, autorization.getClientId());
		statement.setBoolean(4, autorization.isLocked());
		statement.setInt(5, autorization.getClientId());
		statement.executeUpdate();
		conn.close();
		LOG.info("End updating autorization for client " + autorization.getClientId());
	}

	@Override
	public List<Autorization> readAll() throws SQLException {
		LOG.info("Start reading all autorizations");
		String request = "SELECT * FROM autorization";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		List<Autorization> autorizations = new ArrayList<>();
		Autorization autorization = null;
		PreparedStatement statement;
		statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			autorization = parseAutorization(rs);
			autorizations.add(autorization);
		}
		conn.close();
		LOG.info("End reading all autorizations");
		return autorizations;
	}

	@Override
	public void delete(int id) throws SQLException {
		LOG.info("Start deleting autorization with id " + id);
		String request = "DELETE FROM autorization WHERE client_id = ?";
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement statement;
		statement = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		statement.setInt(1, id);
		statement.executeUpdate();
		conn.close();
		LOG.info("End deleting account with id " + id);
	}

	/**
	 * Parses autorization from ResultSet.
	 * 
	 * @param rs
	 *            ResultSet.
	 * @return Autorization.
	 * @throws SQLException
	 *             If there are errors during interaction with database.
	 */
	private Autorization parseAutorization(ResultSet rs) throws SQLException {
		LOG.info("Start parsing autorization");
		Autorization autorization = new Autorization();
		try {
			autorization.setLogin(rs.getString("login"));
			autorization.setPassword(rs.getString("password"));
			autorization.setClientId(rs.getInt("client_id"));
			autorization.setLocked(rs.getBoolean("locked"));
			LOG.info("Autorization was succesfully parsed");
		} catch (SQLException e) {
			LOG.error("Cannot parse autorization for user " + rs.getString("login") + " from ResultSet");
			throw new SQLException();
		}
		LOG.info("End parsing account");
		return autorization;
	}

}
