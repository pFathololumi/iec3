package myServiceHandlers;

import java.io.IOException;
import java.io.PrintWriter;

import server.*;
import ir.ramtung.coolserver.ServiceHandler;
import domain.dealing.TransactionType;

public class DepositHandler extends ServiceHandler{

	@Override
	public void execute(PrintWriter out) throws IOException {
		// TODO Auto-generated method stub
		String id = params.get("id");
		Long amount = Long.parseLong(params.get("amount"));
		//validation?
		if (StockMarket.getInstance().containCustomer(id)){
			StockMarket.getInstance().executeFinancialTransaction(id, TransactionType.DEPOSIT,amount);
			out.println("Successful");
		}
		else
			out.println("Unknown user id");
	}
	
}
