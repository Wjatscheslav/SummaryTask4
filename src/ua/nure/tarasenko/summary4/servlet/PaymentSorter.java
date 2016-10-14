package ua.nure.tarasenko.summary4.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.sort.PaymentSortOrder;
import ua.nure.tarasenko.summary4.sort.SortOrder;
import ua.nure.tarasenko.summary4.util.Constants;

/**
 * Servlet provides methods for sorting payments.
 * 
 * @author Tarasenko
 *
 */
public class PaymentSorter extends HttpServlet {

	private static final Logger LOG = Logger.getLogger(PaymentSorter.class);

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
	 * Sets sort type for payments.
	 * 
	 * @param req
	 *            Request.
	 * @param resp
	 *            Response.
	 * @throws IOException
	 */
	private void setSortType(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Start sorting payments.");
		String sortType = req.getParameter("sorting");
		String ascdesc = req.getParameter("ascdesc");
		System.out.println(sortType);
		if (sortType.equals("PAYMENT_ID")) {
			Constants.PAYMENT_ORDER = PaymentSortOrder.PAYMENT_ID;
			LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Sorting by id.");
		} else {
			Constants.PAYMENT_ORDER = PaymentSortOrder.OPERATION_DATE;
			LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Sorting by date.");
		}
		if (ascdesc.equals("ASC")) {
			Constants.ORDER = SortOrder.ASC;
			LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Sorting ascending.");
		} else {
			Constants.ORDER = SortOrder.DESC;
			LOG.info("LOGIN: " + req.getSession().getAttribute("login") + " Sorting descending.");
		}
		resp.sendRedirect("/SummaryTask4/jsp/show_history.jspx");
	}
}
