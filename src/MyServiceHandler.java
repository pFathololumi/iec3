import java.io.IOException;
import java.io.PrintWriter;

import Client.Customer;
import ir.ramtung.coolserver.ServiceHandler;

public class MyServiceHandler {

}

class AddCustomer extends ServiceHandler{
	
	@Override
	public void execute(PrintWriter out) throws IOException {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(params.get("id"));
        String name = params.get("name");
        String family = params.get("family");
        
        if (Server.customers.containsKey(id))
        	out.println("Repeated id");
        else{
        	Server.customers.put(id, new Customer(id, name, family));
        	out.println("New user is added");
        }
        //Page responsePage = new Page();

	}
	
}

class DepositHandler extends ServiceHandler{

	@Override
	public void execute(PrintWriter out) throws IOException {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(params.get("id"));
		long amount = Long.parseLong(params.get("amount"));
		
		if (Server.customers.containsKey(id)){
			Server.customers.get(id).getCustomerAccount().setBalance("deposit", amount);
			out.println("Successful");
		}
		else
			out.println("Unknown user id");
	}
	
}

class WithdrawHandler extends ServiceHandler{

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