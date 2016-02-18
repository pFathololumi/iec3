package MyServiceHandlers;

import java.io.IOException;
import java.io.PrintWriter;

import Server.*;
import ir.ramtung.coolserver.ServiceHandler;

public class WithdrawHandler extends ServiceHandler{

	@Override
	public void execute(PrintWriter out) throws IOException {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(params.get("id"));
		long amount = Long.parseLong(params.get("amount"));
		
		if (Server.customers.containsKey(id)){
			if(Server.customers.get(id).getCustomerAccount().isEnoughMoney(amount)){
				Server.customers.get(id).getCustomerAccount().setBalance("withdraw", amount);
				out.println("Successful");
			}
			else
				out.println("Not enough money");
		}
		else
			out.println("Unknown user id");
	}
	
}