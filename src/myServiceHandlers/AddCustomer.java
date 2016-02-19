package myServiceHandlers;

import java.io.IOException;
import java.io.PrintWriter;

import server.*;
import domain.Customer;
import ir.ramtung.coolserver.ServiceHandler;

public class AddCustomer extends MyServiceHandler{
	
	@Override
	public int executeByStatus(PrintWriter out) throws IOException {
		// TODO Auto-generated method stub
		String id = params.get("id");
        String name = params.get("name");
        String family = params.get("family");
        if(id==null || id.isEmpty() || name==null || name.isEmpty()
				|| family==null || family.isEmpty()){
			out.println("Mismatched Parameters");
			return 404;
		}
        if (StockMarket.getInstance().containCustomer(id))
        	out.println("Repeated id");
        else{
        	StockMarket.getInstance().addNewCustomer(new Customer(id, name, family));
        	out.println("New user is added");
        }

		return 200;
	}
	
}