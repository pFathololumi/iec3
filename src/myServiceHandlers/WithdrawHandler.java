package myServiceHandlers;

import java.io.IOException;
import java.io.PrintWriter;

import server.*;
import ir.ramtung.coolserver.ServiceHandler;
import domain.dealing.TransactionType;

public class WithdrawHandler extends ServiceHandler{

	@Override
	public void execute(PrintWriter out) throws IOException {
		// TODO Auto-generated method stub
		String id = params.get("id");
		Long amount = Long.parseLong(params.get("amount"));
		
		if (StockMarket.getInstance().containCustomer(id)){
			if(StockMarket.getInstance().customerHasEnoughMoney(id,amount)){
				StockMarket.getInstance().executeFinancialTransaction(id, TransactionType.WITHDRAW,amount);
				out.println("Successful");
			}
			else
				out.println("Not enough money");
		}
		else
			out.println("Unknown user id");
	}
	
}