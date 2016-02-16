package Client;

public class Account{
	private long balance;

	public Account(){
		setBalance("", 0);
	}
	
	public long getBalance() {
		return balance;
	}

	public void setBalance(String type,long monetaryUnit) {
		if("deposit".equals(type))
			this.balance += monetaryUnit;
		else if("withdraw".equals(type))
			this.balance -= monetaryUnit;
		else
			this.balance = monetaryUnit;
	}
	
	public boolean isEnoughMoney(long money){
		if(money <= this.balance)
			return true;
		return false;
	}
}

