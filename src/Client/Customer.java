package Client;

public class Customer {
	private int id;
	private String name;
	private String family;
	private Account customerAccount;
	
	public Customer(int id,String n,String f){
		this.id = id;
		this.name = n;
		this.family = f;
		this.customerAccount = new Account();
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getFamily() {
		return family;
	}

	public Account getCustomerAccount() {
		return customerAccount;
	}

}

