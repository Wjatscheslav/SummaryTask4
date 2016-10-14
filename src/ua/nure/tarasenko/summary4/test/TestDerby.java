package ua.nure.tarasenko.summary4.test;

import java.sql.SQLException;

import ua.nure.tarasenko.summary4.db.DaoFactory;
import ua.nure.tarasenko.summary4.db.GenericDao;
import ua.nure.tarasenko.summary4.db.derby.DerbyAutorizationDao;
import ua.nure.tarasenko.summary4.db.derby.DerbyClientDao;
import ua.nure.tarasenko.summary4.db.derby.DerbyCreditCardDao;
import ua.nure.tarasenko.summary4.db.derby.DerbyDaoFactory;
import ua.nure.tarasenko.summary4.entity.Account;
import ua.nure.tarasenko.summary4.entity.Client;
import ua.nure.tarasenko.summary4.entity.CreditCard;
import ua.nure.tarasenko.summary4.task.SumCalculator;

public class TestDerby {

	public static void main(String[] args) throws SQLException {
	}

	public static void testClient()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		DaoFactory factory = new DerbyDaoFactory();
		DerbyClientDao dao = (DerbyClientDao) factory.getClientDao();
		System.out.println("--------------------------");
		System.out.println("----------CLIENT----------");
		System.out.println("--------------------------");
		System.out.println(dao.read(1));
		System.out.println(dao.read(2));
		System.out.println(dao.read(3));
		System.out.println(dao.readAll());
		Client client = new Client();
		client.setName(1, "Имя");
		client.setSurname(1, "Фамилия");
		client.setName(2, "Name");
		client.setSurname(2, "Surname");
		client.setBornDate("31.12.1995");
		client.setTelephoneNumber("+380671111111");
		dao.create(client);
		System.out.println(dao.readAll());
		Client forUpdate = dao.read(4);
		forUpdate.setTelephoneNumber("+380999998877");
		dao.update(forUpdate);
		System.out.println(dao.readAll());
	}

	public static void testCreditCard()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		DaoFactory factory = new DerbyDaoFactory();
		DerbyCreditCardDao dao = (DerbyCreditCardDao) factory.getCreditCardDao();
		System.out.println("-------------------------------");
		System.out.println("----------CREDIT CARD----------");
		System.out.println("-------------------------------");
		System.out.println(dao.read(1));
		System.out.println(dao.read(2));
		System.out.println(dao.read(3));
		System.out.println(dao.readAll());
		CreditCard card = new CreditCard();
		card.setClientId(2);
		card.setAccountId(2);
		card.setPin(2222);
		dao.create(card);
		System.out.println(dao.readAll());
		card = dao.read(4);
		card.setPin(4444);
		dao.update(card);
		System.out.println(dao.readAll());
		dao.delete(4);
		System.out.println(dao.readAll());
	}

	public static void testAccount()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		DaoFactory factory = new DerbyDaoFactory();
		GenericDao<Account> dao = factory.getAccountDao();
		System.out.println("-------------------------------");
		System.out.println("----------Account----------");
		System.out.println("-------------------------------");
		System.out.println(dao.read(1));
		System.out.println(dao.read(2));
		System.out.println(dao.read(3));
	}

	public static void testAutorization()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		DaoFactory factory = new DerbyDaoFactory();
		DerbyAutorizationDao dao = (DerbyAutorizationDao) factory.getAutorizationDao();
		System.out.println(dao.read("MariaMaria"));
		System.out.println(dao.readAll());
	}
}
