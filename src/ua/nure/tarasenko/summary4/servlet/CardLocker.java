package ua.nure.tarasenko.summary4.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.commands.LockCardCommand;
import ua.nure.tarasenko.summary4.util.Pages;

/**
 * Provides methods for locking and unlocking cards.
 * 
 * @author Tarasenko
 *
 */
public class CardLocker extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(CardLocker.class);

	private static final long serialVersionUID = 1L;

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
	 * Locks or unlocks the card.
	 * 
	 * @param req
	 *            Request.
	 * @param resp
	 *            Response.
	 * @throws ServletException
	 * @throws IOException
	 */
	private void doLock(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Start locking/unlocking card.");
		String page = null;
		LockCardCommand command = new LockCardCommand();
		page = command.execute(req, resp);
		if (page.equals(Pages.LOCK_ERROR_PAGE)) {
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
			dispatcher.forward(req, resp);
			LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Card was not locked/unlocked.");
		} else {
			resp.sendRedirect(page);
			LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Card was locked/unlocked.");
		}
	}
}
