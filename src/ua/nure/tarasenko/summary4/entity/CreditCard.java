package ua.nure.tarasenko.summary4.entity;

public class CreditCard {

	private String cardId;
	private int clientId;
	private long accountId;
	private int pin;
	private boolean locked;

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

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

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	@Override
	public String toString() {
		return "CreditCard [cardId=" + cardId + ", clientId=" + clientId + ", accountId=" + accountId + ", pin=" + pin
				+ ", locked=" + locked + "]";
	}

}
