package ua.nure.tarasenko.summary4.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.commands.LoginCommand;

/**
 * Provides methods for checking login and password.
 * 
 * @author Tarasenko
 *
 */
public class LoginController extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(LoginController.class);

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
		checkClient(req, resp);
		LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " End doPost().");
	}

	/**
	 * Checks client by login and password.
	 * 
	 * @param req
	 *            Request.
	 * @param resp
	 *            Response.
	 * @throws ServletException
	 * @throws IOException
	 */
	private void checkClient(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Start checking client.");
		LoginCommand command = new LoginCommand();
		String page = null;
		page = command.execute(req, resp);
		if (page.equals("/jsp/login_error.jspx")) {
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
			dispatcher.forward(req, resp);
			LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Client is not valid.");
		} else {
			resp.sendRedirect(page);
			LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Client is valid.");
		}
	}
}
