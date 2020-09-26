package domain;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Customer implements Serializable {

	private String id;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("customer_code")
	private String customerCode;

	@SerializedName("customer_group_id")
	private String group;

	@SerializedName("email")
	private String email;

	public Customer() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "Customer{" + "id=" + getId() + ", firstName=" + getFirstName() + ", lastName=" + getLastName() +
				", customerCode=" + getCustomerCode() + ", group=" + getGroup() + ", email=" + getEmail() + '}';
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}


	public Customer generateVendCustomer(String fname, String lname, String customerCode, String email) {
		Customer c = new Customer();
		// 'Regular Customer' group
		c.setGroup("0afa8de1-147c-11e8-edec-2b197906d816");
		c.setFirstName(fname);
		c.setLastName(lname);
		c.setCustomerCode(customerCode);
		c.setEmail(email);
		return c;
	}

	public Customer generateVendCustomer(Account account) {
		Customer c = new Customer();
		// 'Regular Customer' group
		c.setGroup("0afa8de1-147c-11e8-edec-2b197906d816");
		c.setCustomerCode(account.getUsername());
		c.setFirstName(account.getFirstName());
		c.setLastName(account.getLastName());
		c.setEmail(account.getEmail());
		return c;
	}

	public Customer changeCustomerGroup(Customer c, String newGroup) {
		c.setGroup(newGroup);
		return c;
	}
}
