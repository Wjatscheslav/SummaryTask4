package ua.nure.tarasenko.summary4.commands;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.db.DaoFactory;
import ua.nure.tarasenko.summary4.db.GenericDao;
import ua.nure.tarasenko.summary4.db.derby.DerbyDaoFactory;
import ua.nure.tarasenko.summary4.entity.CreditCard;
import ua.nure.tarasenko.summary4.util.Pages;

/**
 * Provides method for locking card by card id.
 * 
 * @author Tarasenko
 */
public class LockCardCommand implements Command {

	private static final Logger LOG = Logger.getLogger(LockCardCommand.class);
	private static HttpSession SESSION;

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		SESSION = req.getSession(true);
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Starting execution LockCardCommand");
		HttpSession session = req.getSession();
		String number = req.getParameter("number");
		String page = null;
		CreditCard card = null;
		try {
			card = getCard(number);
			LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Card was found");
		} catch (SQLException e) {
			LOG.error("LOGIN: " + SESSION.getAttribute("login") + " Cannot find card");
			req.setAttribute("errorMessage", "Cannot find this card");
			page = Pages.LOCK_ERROR_PAGE;
			return page;
		}
		String lock = req.getParameter("lock");
		if (lock.equals("true")) {
			card.setLocked(true);
		} else {
			card.setLocked(false);
		}
		try {
			updateCard(card);
			page = Pages.CARD_LOCKED_PAGE;
			session.setAttribute("number_lock", number);
			session.setAttribute("action", lock);
			LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Card was locked/unlocked.");
		} catch (SQLException e) {
			LOG.error("LOGIN: " + SESSION.getAttribute("login") + " Card was not locked/unlocked.");
			req.setAttribute("errorMessage", "Cannot lock/unlock this card");
			page = Pages.LOCK_ERROR_PAGE;
		}
		return page;
	}

	/**
	 * Updates card in DB.
	 * 
	 * @param card
	 *            Card for updating
	 * @throws SQLException
	 *             If there are errors during updating.
	 */
	private void updateCard(CreditCard card) throws SQLException {
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Start updating card.");
		DaoFactory factory = new DerbyDaoFactory();
		GenericDao<CreditCard> dao = factory.getCreditCardDao();
		dao.update(card);
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " End updating card.");
	}

	/**
	 * Returns card by card id.
	 * 
	 * @param number
	 *            Card id.
	 * @return Credit card.
	 * @throws SQLException
	 *             If there are errors during this operation.
	 */
	private CreditCard getCard(String number) throws SQLException {
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Start geting card.");
		CreditCard card = new CreditCard();
		DaoFactory factory = new DerbyDaoFactory();
		GenericDao<CreditCard> dao = factory.getCreditCardDao();
		card = dao.read(Long.parseLong(number));
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " End getting card.");
		return card;
	}
}
