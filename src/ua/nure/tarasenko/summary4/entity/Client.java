package ua.nure.tarasenko.summary4.entity;

import java.util.HashMap;
import java.util.Map;

public class Client {

	private int clientId;
	private Map<Integer, String> names = new HashMap<>();
	private Map<Integer, String> surnames = new HashMap<>();
	private String bornDate;
	private String telephoneNumber;
	private Role role;

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public Map<Integer, String> getNames() {
		return names;
	}

	public void setName(int languageId, String name) {
		this.names.put(languageId, name);
	}

	public Map<Integer, String> getSurnames() {
		return surnames;
	}

	public void setSurname(int languageId, String surname) {
		this.surnames.put(languageId, surname);
	}

	public String getBornDate() {
		return bornDate;
	}

	public void setBornDate(String bornDate) {
		this.bornDate = bornDate;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Client [clientId=" + clientId + ", names=" + names + ", surnames=" + surnames + ", bornDate=" + bornDate
				+ ", telephoneNumber=" + telephoneNumber + "]";
	}

}
