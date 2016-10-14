package ua.nure.tarasenko.summary4.entity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import ua.nure.tarasenko.summary4.util.Constants;

public class Account {

	private long accountId;
	private int clientId;
	private Map<Integer, String> names = new HashMap<>();
	private BigDecimal amount;

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public Map<Integer, String> getNames() {
		return names;
	}
	
	public String getName() {
		return names.get(Constants.LANGUAGE_ID);
	}

	public void setName(int languageId, String name) {
		this.names.put(languageId, name);
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", clientId=" + clientId + ", names=" + names + ", amount=" + amount
				+ "]";
	}
	
	

}
