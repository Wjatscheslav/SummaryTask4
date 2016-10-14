package ua.nure.tarasenko.summary4.commands;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.db.DaoFactory;
import ua.nure.tarasenko.summary4.db.derby.DerbyAutorizationDao;
import ua.nure.tarasenko.summary4.db.derby.DerbyDaoFactory;
import ua.nure.tarasenko.summary4.entity.Autorization;
import ua.nure.tarasenko.summary4.util.Pages;

/**
 * Provides method for locking user by login.
 * 
 * @author user
 */
public class LockUserCommand implements Command {

	private static final Logger LOG = Logger.getLogger(LockUserCommand.class);
	private static HttpSession SESSION;

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		SESSION = req.getSession();
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Starting execution LockUserCommand");
		String login = req.getParameter("login");
		String page = null;
		Autorization autorization = null;
		try {
			autorization = getAutorization(login);
			LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Autorization was found.");
		} catch (SQLException e) {
			LOG.error("LOGIN: " + SESSION.getAttribute("login") + " Cannot find autorization.");
			req.setAttribute("errorMessage", "Cannot find this client");
			page = Pages.LOCK_ERROR_PAGE;
			return page;
		}
		String lock = req.getParameter("lock");
		if (lock.equals("true")) {
			autorization.setLocked(true);
		} else {
			autorization.setLocked(false);
		}
		try {
			updateAutorization(autorization);
			page = Pages.USER_LOCKED_PAGE;
			SESSION.setAttribute("login_lock", login);
			SESSION.setAttribute("action", lock);
			LOG.info("LOGIN: " + SESSION.getAttribute("login") + " User was locked/unlocked.");
		} catch (SQLException e) {
			LOG.error("LOGIN: " + SESSION.getAttribute("login") + " Cannot lock/unlock this user.");
			req.setAttribute("errorMessage", "Cannot lock/unlock this client");
			page = Pages.LOCK_ERROR_PAGE;
		}
		return page;
	}

	/**
	 * Returns instance of class Autorization by user login.
	 * 
	 * @param login
	 *            User login.
	 * @return Autorization.
	 * @throws SQLException
	 *             If there are errors during this operation.
	 */
	private Autorization getAutorization(String login) throws SQLException {
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Start getting autorization.");
		Autorization autorization = new Autorization();
		DaoFactory factory = new DerbyDaoFactory();
		DerbyAutorizationDao dao = (DerbyAutorizationDao) factory.getAutorizationDao();
		autorization = dao.read(login);
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " End getting autorization.");
		return autorization;
	}

	/**
	 * Updates authorization in DB.
	 * 
	 * @param authorization
	 *            Authorization for updating
	 * @throws SQLException
	 *             If there are errors during updating.
	 */
	private void updateAutorization(Autorization autorization) throws SQLException {
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Start updating autorization.");
		DaoFactory factory = new DerbyDaoFactory();
		DerbyAutorizationDao dao = (DerbyAutorizationDao) factory.getAutorizationDao();
		dao.update(autorization);
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Start updating autorization.");
	}
}
