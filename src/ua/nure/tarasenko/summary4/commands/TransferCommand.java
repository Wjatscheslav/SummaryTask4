package ua.nure.tarasenko.summary4.commands;

import java.math.BigDecimal;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.tarasenko.summary4.db.DaoFactory;
import ua.nure.tarasenko.summary4.db.GenericDao;
import ua.nure.tarasenko.summary4.db.derby.DerbyDaoFactory;
import ua.nure.tarasenko.summary4.db.derby.TransferManager;
import ua.nure.tarasenko.summary4.entity.Account;
import ua.nure.tarasenko.summary4.entity.CreditCard;
import ua.nure.tarasenko.summary4.exception.WasLockedException;
import ua.nure.tarasenko.summary4.util.Pages;

/**
 * Provides methods for transferring money from one account to another.
 * 
 * @author Tarasenko
 *
 */
public class TransferCommand implements Command {

	private static final Logger LOG = Logger.getLogger(TransferCommand.class);
	private static HttpSession SESSION;

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) {
		SESSION = req.getSession(true);
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Starting execution TransferCommand");
		String page = null;
		CreditCard sendCard = null;
		CreditCard receiveCard = null;
		String sendCardNumberStr = SESSION.getAttribute("from").toString();
		String receiveCardNumberStr = SESSION.getAttribute("to").toString();
		BigDecimal amount = BigDecimal.valueOf(Long.parseLong(SESSION.getAttribute("am").toString()));
		long sendCardNumber = Long.parseLong(sendCardNumberStr);
		long receiveCardNumber = Long.parseLong(receiveCardNumberStr);
		if (receiveCardNumber == sendCardNumber) {
			req.setAttribute("errorMessage", "The same card number");
			page = Pages.TRANSFER_ERROR_PAGE;
			LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Send and receive card numbers are the same");
			return page;
		}
		receiveCard = getCreditCard(receiveCardNumber);
		if (receiveCard.getCardId() == null) {
			req.setAttribute("errorMessage", "This card doesn't exist");
			page = Pages.TRANSFER_ERROR_PAGE;
			LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Receive card" + receiveCard.getCardId()
					+ "doesnt exist");
			return page;
		}
		try {
			isLocked(receiveCard);
		} catch (WasLockedException e) {
			page = Pages.TRANSFER_ERROR_PAGE;
			req.setAttribute("errorMessage", "Receive card was locked");
			return page;
		}
		sendCard = getCreditCard(sendCardNumber);
		try {
			isLocked(sendCard);
		} catch (WasLockedException e) {
			page = Pages.TRANSFER_ERROR_PAGE;
			req.setAttribute("errorMessage", "Send card was locked");
			return page;
		}
		long sendAccountId = sendCard.getAccountId();
		long receiveAccountId = receiveCard.getAccountId();
		Account sendAccount = getAccount(sendAccountId);
		Account receiveAccount = getAccount(receiveAccountId);
		BigDecimal sendAccountAmount = sendAccount.getAmount();
		if (sendAccountAmount.compareTo(amount) >= 0) {
			BigDecimal receiveAccountAmount = receiveAccount.getAmount();
			sendAccountAmount = sendAccountAmount.subtract(amount);
			sendAccount.setAmount(sendAccountAmount);
			receiveAccountAmount = receiveAccountAmount.add(amount);
			receiveAccount.setAmount(receiveAccountAmount);
			TransferManager man = new TransferManager();
			try {
				man.doTransfer(sendAccount, receiveAccount);
			} catch (SQLException e) {
				LOG.error("LOGIN: " + SESSION.getAttribute("login") + "Cannot make a transfer");
			}
			man.addTransferToDB(sendAccountId, receiveAccountId, amount);
			page = Pages.SUCCEED_TRANSFER_PAGE;
			LOG.info("LOGIN: " + SESSION.getAttribute("login") + "Transfer succeed");
		} else {
			req.setAttribute("errorMessage", "Not enough money in the account");
			page = Pages.TRANSFER_ERROR_PAGE;
			LOG.info("LOGIN: " + SESSION.getAttribute("login") + "Not enough money int the account: " + sendAccountId);
		}
		return page;
	}

	/**
	 * Returns credit card by card id.
	 * 
	 * @param cardNumber
	 *            Card id.
	 * @return Credit card.
	 */
	private CreditCard getCreditCard(long cardNumber) {
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Start getting credit card");
		DaoFactory factory = new DerbyDaoFactory();
		CreditCard card = null;
		GenericDao<CreditCard> dao = factory.getCreditCardDao();
		try {
			card = dao.read(cardNumber);
		} catch (SQLException e) {
		}
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " End getting credit card");
		return card;
	}

	/**
	 * Returns account by account id.
	 * 
	 * @param accountId
	 *            Account id.
	 * @return Account.
	 */
	private Account getAccount(long accountId) {
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Start getting account");
		DaoFactory factory = new DerbyDaoFactory();
		Account account = null;
		GenericDao<Account> dao = factory.getAccountDao();
		try {
			account = dao.read(accountId);
		} catch (SQLException e) {
		}
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " End getting account");
		return account;
	}

	/**
	 * This method checks, is credit card locked or not.
	 * 
	 * @param card
	 *            Credit card.
	 * @return True if credit card locked. Otherwise - false.
	 * @throws WasLockedException
	 *             If credit card was locked.
	 */
	private boolean isLocked(CreditCard card) throws WasLockedException {
		LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Start checking account - locked or no.");
		if (!card.isLocked()) {
			LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Account not locked.");
			return false;
		} else {
			LOG.info("LOGIN: " + SESSION.getAttribute("login") + " Account locked.");
			throw new WasLockedException();
		}
	}
}
