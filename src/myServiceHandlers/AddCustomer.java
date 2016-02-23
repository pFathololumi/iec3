package myServiceHandlers;

import java.io.IOException;
import java.io.PrintWriter;

import logger.MyLogger;
import server.*;
import domain.Customer;

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
        if (StockMarket.getInstance().containCustomer(id)) {
			out.println("Repeated id");
			MyLogger.info("ID '"+id+"' is repeated.");
		}else{
        	StockMarket.getInstance().addNewCustomer(new Customer(id, name, family));
        	out.println("New user is added");
			MyLogger.info("New User with id '"+id+"' is added");
        }

		return 200;
	}
	
}