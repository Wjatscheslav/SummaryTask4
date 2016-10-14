package ua.nure.tarasenko.summary4.commands;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.db.DaoFactory;
import ua.nure.tarasenko.summary4.db.derby.DerbyAutorizationDao;
import ua.nure.tarasenko.summary4.db.derby.DerbyClientDao;
import ua.nure.tarasenko.summary4.db.derby.DerbyDaoFactory;
import ua.nure.tarasenko.summary4.entity.Autorization;
import ua.nure.tarasenko.summary4.entity.Client;
import ua.nure.tarasenko.summary4.entity.Role;
import ua.nure.tarasenko.summary4.exception.WasLockedException;
import ua.nure.tarasenko.summary4.util.Constants;
import ua.nure.tarasenko.summary4.util.Pages;

/**
 * Provide methods for checking login for and redirecting to expected page.
 * 
 * @author Tarasenko
 *
 */
public class LoginCommand implements Command {

	private static final Logger LOG = Logger.getLogger(LoginCommand.class);
	private static HttpSession SESSION;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		SESSION = request.getSession(true);
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Starting execution LoginCommand");
		String page = null;
		HttpSession session = request.getSession(true);
		setLanguage(request);
		String login = request.getParameter("login");
		String pass = request.getParameter("password");
		int clientId = 0;
		try {
			clientId = checkLogin(login, pass);
		} catch (WasLockedException e) {
			page = Pages.LOGIN_ERROR_PAGE;
			request.setAttribute("errorMessage", "Client was locked");
			return page;
		} catch (SQLException e) {
			page = Pages.LOGIN_ERROR_PAGE;
			request.setAttribute("errorMessage", "User with this login doesnt exist");
			return page;
		}
		if (clientId > 0) {
			session.setAttribute("login", login);
			session.setAttribute("id", clientId);
			Role role = getRole(clientId);
			switch (role) {
			case ADMIN:
				page = Pages.MAIN_ADMIN_PAGE;
				break;
			case USER:
				page = Pages.MAIN_CLIENT_PAGE;
				break;
			default:
				page = Pages.LOGIN_ERROR_PAGE;
				request.setAttribute("errorMessage", "Unknown role");
			}
			LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Login and password for user " + login
					+ " are correct");
		} else {
			request.setAttribute("errorMessage", "Login or password is incorrect");
			page = Pages.LOGIN_ERROR_PAGE;
			LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Login or password is incorrect");
		}
		LOG.info("LOGIN: " + login + "\tEnding execution LoginCommand");
		return page;
	}

	/**
	 * Checks login and returns uer id.
	 * 
	 * @param login
	 *            Login entered from the keyboard.
	 * @param password
	 *            Password entered from the keyboard.
	 * @return Client id.
	 * @throws WasLockedException
	 *             If user was locked by admin.
	 * @throws SQLException
	 *             If there are errors during the work with DB.
	 */
	private int checkLogin(String login, String password) throws WasLockedException, SQLException {
		LOG.info("LOGIN: " + login + " Checking login and password");
		int clientId = -1;
		DaoFactory factory = new DerbyDaoFactory();
		DerbyAutorizationDao dao = (DerbyAutorizationDao) factory.getAutorizationDao();
		Autorization autorization = new Autorization();
		autorization = dao.read(login);
		if (autorization != null && autorization.getPassword() != null) {
			if (autorization.getPassword().equals(password)) {
				if (!isLocked(autorization)) {
					clientId = autorization.getClientId();
				}
			}
		}
		LOG.info("LOGIN: " + login + " Checking login and password was ended");
		return clientId;
	}

	/**
	 * This method checks, is user locked or not.
	 * 
	 * @param autorization
	 *            User autorization.
	 * @return True if user was locked. Otherwise - false.
	 * @throws WasLockedException
	 *             If user was locked by admin.
	 */
	private boolean isLocked(Autorization autorization) throws WasLockedException {
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Start checking client lock.");
		if (!autorization.isLocked()) {
			LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Client doesnt lock.");
			return false;
		} else {
			LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Client locked.");
			throw new WasLockedException();
		}
	}

	/**
	 * Returns role for current client.
	 * 
	 * @param clientId
	 *            Client id.
	 * @return Role for current client.
	 */
	private Role getRole(int clientId) {
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Start getting role.");
		DaoFactory factory = new DerbyDaoFactory();
		DerbyClientDao dao = (DerbyClientDao) factory.getClientDao();
		Client client = new Client();
		try {
			client = dao.read(clientId);
			LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Role was found.");
		} catch (SQLException e) {
			LOG.error("Cannot define the role for client: " + clientId);
		}
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " End getting role.");
		return client.getRole();
	}

	/**
	 * Sets language for current session.
	 * 
	 * @param request
	 */
	private void setLanguage(HttpServletRequest request) {
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Setting the language");
		HttpSession session = request.getSession(true);
		if (session.getAttribute("language") != null) {
			if (session.getAttribute("language").equals("ru")) {
				Constants.LANGUAGE_ID = 1;
				LOG.info("Language RU was setted");
			}
			if (session.getAttribute("language").equals("en")) {
				Constants.LANGUAGE_ID = 2;
				LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Language EN was setted");
			}
		}
	}

}
