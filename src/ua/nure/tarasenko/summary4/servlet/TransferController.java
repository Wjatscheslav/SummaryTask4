package ua.nure.tarasenko.summary4.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.commands.TransferCommand;

/**
 * Provides methods for making transfers between accounts.
 * 
 * @author Tarasenko
 *
 */
public class TransferController extends HttpServlet {
	
	private static final Logger LOG = Logger.getLogger(TransferController.class);

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
		doTransfer(req, resp);
		LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " End doPost().");
	}

	/**
	 * Makes transfer between accounts.
	 * 
	 * @param req
	 *            Request.
	 * @param resp
	 *            Response.
	 * @throws ServletException
	 * @throws IOException
	 */
	private void doTransfer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Start doing transfer.");
		String page = null;
		TransferCommand command = new TransferCommand();
		page = command.execute(req, resp);
		if (page.equals("/jsp/transfer_error.jspx")) {
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
			dispatcher.forward(req, resp);
			LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Transfer was failed.");
		} else {
			resp.sendRedirect(page);
			LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Transfer succesfull.");
		}
	}
}
