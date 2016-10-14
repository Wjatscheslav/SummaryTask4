package ua.nure.tarasenko.summary4.db;

import java.sql.Connection;

import ua.nure.tarasenko.summary4.entity.Account;
import ua.nure.tarasenko.summary4.entity.Autorization;
import ua.nure.tarasenko.summary4.entity.Client;
import ua.nure.tarasenko.summary4.entity.CreditCard;
import ua.nure.tarasenko.summary4.entity.Payment;

/**
 * This interface provides methods for becoming DAO objects for access to tables
 * from database.
 * 
 * @author Tarasenko
 */
public interface DaoFactory {

	/**
	 * Returns objects for access to account table.
	 * 
	 * @return DAO for account table.
	 */
	public GenericDao<Account> getAccountDao();

	/**
	 * Returns objects for access to client table.
	 * 
	 * @return DAO for client table.
	 */
	public GenericDao<Client> getClientDao();

	/**
	 * Returns objects for access to payment table.
	 * 
	 * @return DAO for payment table.
	 */
	public GenericDao<Payment> getPaymentDao();

	/**
	 * Returns objects for access to credit card table.
	 * 
	 * @return DAO for credit card table.
	 */
	public GenericDao<CreditCard> getCreditCardDao();

	/**
	 * Returns objects for access to authorization table.
	 * 
	 * @return DAO for authorization table.
	 */
	public GenericDao<Autorization> getAutorizationDao();

	/**
	 * This method returns connection for accessing to database.
	 * 
	 * @return
	 */
	public Connection getConnection();

}
