package domain;

import java.io.Serializable;

public class Account implements Serializable {

	private String id;
	private String email;
	private String username;
	private String firstName;
	private String lastName;
	private String group;
	private String uri;

	public Account() {
	}

	public Account(String id, String email, String username, String firstName, String lastName, String group) {
		this.id = id;
		this.email = email;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.group = group;
	}

	public Account generateAccount(String id, String firstName, String lastName, String customerCode, String group, String email) {
		Account a = new Account();
		a.setId(id);
		a.setGroup(group);
		a.setFirstName(firstName);
		a.setLastName(lastName);
		a.setUsername(customerCode);
		a.setEmail(email);
		return a;
	}

	public Account generateAccount(Customer c) {
		Account a = new Account();
		a.setId(c.getId());
		a.setGroup(c.getGroup());
		a.setFirstName(c.getFirstName());
		a.setLastName(c.getLastName());
		a.setUsername(c.getCustomerCode());
		a.setEmail(c.getEmail());
		return a;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
