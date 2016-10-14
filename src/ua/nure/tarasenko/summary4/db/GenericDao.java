package ua.nure.tarasenko.summary4.db;

import java.sql.SQLException;
import java.util.List;

/**
 * This interface provides methods for transfer information to/from database.
 * 
 * @author Tarasenko
 *
 * @param <T>
 *            Object of class, that corresponds to the table from database.
 */
public interface GenericDao<T> {

	/**
	 * This method adds new object to database.
	 * 
	 * @param t
	 *            Object for adding to database.
	 * @throws SQLException
	 *             If there are errors during interaction with database.
	 */
	public void create(T t) throws SQLException;

	/**
	 * This method reads the object from database by id.
	 * 
	 * @param id
	 *            Id for reading object.
	 * @return Object from database.
	 * @throws SQLException
	 *             If there are errors during interaction with database.
	 */
	public T read(long id) throws SQLException;

	/**
	 * This method updates existing object in database.
	 * 
	 * @param t
	 *            Object for updating.
	 * @throws SQLException
	 *             If there are errors during interaction with database.
	 */
	public void update(T t) throws SQLException;

	/**
	 * This method read all objects of specified class from database.
	 * 
	 * @return Collection with objects.
	 * @throws SQLException
	 *             If there are errors during interaction with database.
	 */
	public List<T> readAll() throws SQLException;

	/**
	 * This method deletes object from database.
	 * 
	 * @param id
	 *            Object id.
	 * @throws SQLException
	 *             If there are errors during interaction with database.
	 */
	void delete(int id) throws SQLException;

}
