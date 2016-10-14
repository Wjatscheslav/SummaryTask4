package ua.nure.tarasenko.summary4.db.bean;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.db.DaoFactory;
import ua.nure.tarasenko.summary4.db.derby.DerbyClientDao;
import ua.nure.tarasenko.summary4.db.derby.DerbyDaoFactory;
import ua.nure.tarasenko.summary4.entity.Client;
import ua.nure.tarasenko.summary4.util.Constants;

public class ClientBean {

	private static final Logger LOG = Logger.getLogger(ClientBean.class);

	private int id;
	private long card;
	private Client client;

	public ClientBean() {
	}

	public void setId(int id) {
		this.id = id;
		loadClient();
	}

	public long getCard() {
		return card;
	}

	public void setCard(long cardId) {
		this.card = cardId;
		loadClientByCardId();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return client.getNames().get(Constants.LANGUAGE_ID);
	}

	public String getSurname() {
		return client.getSurnames().get(Constants.LANGUAGE_ID);
	}

	public String getBornDate() {
		return client.getBornDate();
	}

	public String getTelephoneNumber() {
		return client.getTelephoneNumber();
	}

	private Client loadClient() {
		LOG.info("Start loading client " + id + " from database");
		DaoFactory factory = new DerbyDaoFactory();
		DerbyClientDao dao = (DerbyClientDao) factory.getClientDao();
		try {
			client = dao.read(id);
		} catch (SQLException e) {
			LOG.error("Cannot read client " + id + " from database");
		}
		LOG.info("End loading client " + id);
		return client;
	}
	
	private Client loadClientByCardId() {
		LOG.info("Start loading client by card id " + card + " from database");
		DaoFactory factory = new DerbyDaoFactory();
		DerbyClientDao dao = (DerbyClientDao) factory.getClientDao();
		try {
			client = dao.read(card);
		} catch (SQLException e) {
			LOG.error("Cannot read client " + card + " from database");
		}
		LOG.info("End loading client " + card);
		return client;
	}
}
