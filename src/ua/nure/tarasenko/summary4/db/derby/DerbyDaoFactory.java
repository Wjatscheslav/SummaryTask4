package ua.nure.tarasenko.summary4.db.derby;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import ua.nure.tarasenko.summary4.db.DaoFactory;
import ua.nure.tarasenko.summary4.db.GenericDao;
import ua.nure.tarasenko.summary4.entity.Autorization;

/**
 * Implementation DaoFactory for DerbyDB. Returns DAO objects for different
 * classes.
 * 
 * @author Tarasenko
 */
public class DerbyDaoFactory implements DaoFactory {

	public static final String URL = "jdbc:derby:C:\\Epam_JAVA\\PaySystem";
	public static final String DRIVER_NAME = "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String CLASS_NAME = "ua.nure.tarasenko.summary4.db.derby.DerbyDaoFactory";
	private static BasicDataSource source = null;
	private static DerbyDaoFactory instance = null;

	static {
		source = new BasicDataSource();
		source.setDriverClassName(DRIVER_NAME);
		source.setUrl(URL);
	}

	@Override
	public DerbyAccountDao getAccountDao() {
		return new DerbyAccountDao();
	}

	@Override
	public DerbyClientDao getClientDao() {
		return new DerbyClientDao();
	}

	@Override
	public DerbyPaymentDao getPaymentDao() {
		return new DerbyPaymentDao();
	}

	@Override
	public DerbyCreditCardDao getCreditCardDao() {
		return new DerbyCreditCardDao();
	}

	@Override
	public GenericDao<Autorization> getAutorizationDao() {
		return new DerbyAutorizationDao();
	}

	@Override
	public Connection getConnection() {
		Connection connection = null;
		try {
			connection = source.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * Returns instance of class DerbyDaoFactory.
	 * 
	 * 
	 * @return DerbyDaoFactory.
	 */
	public static DerbyDaoFactory getInstance() {
		if (instance == null) {
			Class<?> daoClass;
			try {
				daoClass = Class.forName(CLASS_NAME);
				instance = (DerbyDaoFactory) daoClass.newInstance();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

}
