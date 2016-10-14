package ua.nure.tarasenko.summary4.entity;

public class Autorization {

	private String login;
	private String password;
	private int clientId;
	private boolean locked;

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	@Override
	public String toString() {
		return "Autorization [login=" + login + ", password=" + password + ", clientId=" + clientId + "]";
	}

}
