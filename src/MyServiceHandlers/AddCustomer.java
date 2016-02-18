package MyServiceHandlers;

import java.io.IOException;
import java.io.PrintWriter;

import Server.*;
import Client.Customer;
import ir.ramtung.coolserver.ServiceHandler;

public class AddCustomer extends ServiceHandler{
	
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