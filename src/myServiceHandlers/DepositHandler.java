package myServiceHandlers;

import java.io.IOException;
import java.io.PrintWriter;

import server.*;
import ir.ramtung.coolserver.ServiceHandler;
import domain.dealing.TransactionType;

public class DepositHandler extends MyServiceHandler{

	@Override
	public int executeByStatus(PrintWriter out) throws IOException {
		// TODO Auto-generated method stub
		String id = params.get("id");
		Long amount =null;
		try{
			amount= Long.parseLong(params.get("amount"));
			if(id==null ||id.isEmpty())
				throw new Exception();
		}catch (Exception e){
			out.println("Mismatched Parameters");
			return 404;
		}

		if (StockMarket.getInstance().containCustomer(id)){
			StockMarket.getInstance().executeFinancialTransaction(id, TransactionType.DEPOSIT,amount);
			out.println("Successful");
		}
		else
			out.println("Unknown user id");
		return 200;
	}
	
}
