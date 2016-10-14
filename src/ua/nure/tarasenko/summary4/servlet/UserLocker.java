package ua.nure.tarasenko.summary4.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.commands.LockUserCommand;

/**
 * Provides methods for locking and unlocking users.
 * 
 * @author Tarasenko
 *
 */
public class UserLocker extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(UserLocker.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Start doGet().");
		doPost(req, resp);
		LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " End doGet().");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Start doPost().");
		doLock(req, resp);
		LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " End doPost().");
	}

	/**
	 * Locks or unlocks user.
	 * 
	 * @param req
	 *            Request.
	 * @param resp
	 *            Response.
	 * @throws ServletException
	 * @throws IOException
	 */
	private void doLock(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Start locking/unlocking user.");
		String page = null;
		LockUserCommand command = new LockUserCommand();
		page = command.execute(req, resp);
		if (page.equals("/jsp/lock_error.jspx")) {
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
			dispatcher.forward(req, resp);
			LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Locking/unlocking user was failed.");
		} else {
			LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Locking/unlocking user was succesful.");
			resp.sendRedirect(page);
		}
	}
}
