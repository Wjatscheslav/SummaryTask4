package ua.nure.tarasenko.summary4.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.sort.AccountSortOrder;
import ua.nure.tarasenko.summary4.util.Constants;

/**
 * Servlet provides methods for sorting accounts.
 * 
 * @author Tarasenko
 *
 */
public class AccountSorter extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(AccountSorter.class);

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
		setSortType(req, resp);
		LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " End doPost().");
	}

	/**
	 * Sets sort type for accounts.
	 * 
	 * @param req
	 *            Request.
	 * @param resp
	 *            Response.
	 * @throws ServletException
	 * @throws IOException
	 */
	private void setSortType(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Start sorting accounts.");
		String sortType = req.getParameter("sorting");
		System.out.println(sortType);
		if (sortType.equals("ID")) {
			Constants.ACCOUNT_ORDER = AccountSortOrder.ID;
			LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Sorting by id.");
		} else if (sortType.equals("NAME")) {
			Constants.ACCOUNT_ORDER = AccountSortOrder.NAME;
			LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Sorting by name.");
		} else {
			Constants.ACCOUNT_ORDER = AccountSortOrder.AMOUNT;
			LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Sorting by amount.");
		}
		resp.sendRedirect("/SummaryTask4/jsp/personal.jspx");
		LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " End sorting accounts.");
	}
}
