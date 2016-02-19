package domain;

import domain.dealing.TransactionType;

public class Customer {
	private String id;
	private String name;
	private String family;
	private Account customerAccount;
	
	public Customer(String id,String n,String f){
		this.id = id;
		this.name = n;
		this.family = f;
		this.customerAccount = new Account();
	}

	public String getId() {
		return id;
	}

	public void executeTransaction(TransactionType type,Long amount){
		customerAccount.executeTransaction(type,amount);
	}
	public Boolean hasEnoughMoney(Long amount){
		return customerAccount.isEnoughMoney(amount);
	}

}

