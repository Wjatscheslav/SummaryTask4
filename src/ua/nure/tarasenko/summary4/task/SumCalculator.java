package ua.nure.tarasenko.summary4.task;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ua.nure.tarasenko.summary4.db.derby.DerbyDaoFactory;

public class SumCalculator {

	public BigDecimal calculateSendSum(long accountId) throws SQLException {
		String request = "SELECT SUM(AMOUNT) FROM PAYMENT WHERE SEND_ACCOUNT = ?";
		BigDecimal sum = null;
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement stmt = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		stmt.setLong(1, accountId);
		ResultSet rs = stmt.executeQuery();
		rs.beforeFirst();
		rs.first();
		sum = rs.getBigDecimal(1);
		return sum;
	}
	
	public BigDecimal calculateReceiveSum(long accountId) throws SQLException {
		String request = "SELECT SUM(AMOUNT) FROM PAYMENT WHERE RECEIVE_ACCOUNT = ?";
		BigDecimal sum = null;
		Connection conn = DerbyDaoFactory.getInstance().getConnection();
		PreparedStatement stmt = conn.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		stmt.setLong(1, accountId);
		ResultSet rs = stmt.executeQuery();
		rs.beforeFirst();
		rs.first();
		sum = rs.getBigDecimal(1);
		return sum;
	}
}
