package ua.nure.tarasenko.summary4.db.bean;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.db.DaoFactory;
import ua.nure.tarasenko.summary4.db.derby.DerbyCreditCardDao;
import ua.nure.tarasenko.summary4.db.derby.DerbyDaoFactory;
import ua.nure.tarasenko.summary4.entity.CreditCard;

public class CreditCardBean {
	
	private static final Logger LOG = Logger.getLogger(CreditCardBean.class);

	private int clientId;
	private List<CreditCard> cards;

	public void setClientId(int clientId) {
		this.clientId = clientId;
		loadCards();
	}

	public List<CreditCard> getCards() {
		return cards;
	}

	private List<CreditCard> loadCards() {
		LOG.info("Start loading cards for client " + clientId + " from database");
		DaoFactory factory = new DerbyDaoFactory();
		DerbyCreditCardDao dao = (DerbyCreditCardDao) factory.getCreditCardDao();
		try {
			cards = dao.readByClientId(clientId);
		} catch (SQLException e) {
			LOG.error("Cannot load cards for client " + clientId);
		}
		LOG.info("End loading accounts for client " + clientId);
		return cards;
	}
}
