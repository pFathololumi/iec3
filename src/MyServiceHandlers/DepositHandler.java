package MyServiceHandlers;

import java.io.IOException;
import java.io.PrintWriter;

import Server.*;
import ir.ramtung.coolserver.ServiceHandler;

public class DepositHandler extends ServiceHandler{

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
