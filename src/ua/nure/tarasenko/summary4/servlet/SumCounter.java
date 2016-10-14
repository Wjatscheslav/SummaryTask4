package ua.nure.tarasenko.summary4.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.tarasenko.summary4.db.DaoFactory;
import ua.nure.tarasenko.summary4.db.derby.DerbyAccountDao;
import ua.nure.tarasenko.summary4.db.derby.DerbyDaoFactory;

public class SumCounter extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		calculateSum(req, resp);
	}

	private void calculateSum(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestSend = "SELECT SUM(AMOUNT) FROM PAYMENT WHERE SEND_ACCOUNT = ?";
		String requestReceive = "SELECT SUM(AMOUNT) FROM PAYMENT WHERE RECEIVE_ACCOUNT = ?";
		DaoFactory factory = new DerbyDaoFactory();
		DerbyAccountDao dao = (DerbyAccountDao) factory.getAccountDao();
		long id = Long.parseLong(req.getParameter("accId"));
		try {
			BigDecimal sendSum = dao.calculateSum(id, requestSend);
			BigDecimal receiveSum = dao.calculateSum(id, requestReceive);
			req.setAttribute("sendSum", sendSum);
			req.setAttribute("receiveSum", receiveSum);
		} catch (SQLException e) {
			req.setAttribute("errorMessage", "Cannot calculate sum for this account");
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/sum_error.jspx");
			dispatcher.forward(req, resp);
		}
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/jsp/sum.jspx");
		dispatcher.forward(req, resp);
	}
}
