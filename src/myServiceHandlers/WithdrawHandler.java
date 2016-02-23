package myServiceHandlers;

import java.io.IOException;
import java.io.PrintWriter;

import logger.MyLogger;
import server.*;
import domain.dealing.TransactionType;

public class WithdrawHandler extends MyServiceHandler{

	@Override
	public int executeByStatus(PrintWriter out) throws IOException {
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
			if(StockMarket.getInstance().customerHasEnoughMoney(id,amount)){
				StockMarket.getInstance().executeFinancialTransaction(id, TransactionType.WITHDRAW,amount);
				out.println("Successful");
				MyLogger.info("Withdraw '"+amount+"' for user ID '"+id+"' ");
			}
			else {
				out.println("Not enough money");
				MyLogger.info("Not enough money for user id '"+id+"' for amount of '"+amount+"'");
			}
		}
		else {
			out.println("Unknown user id");
			MyLogger.info("Unknown user id '"+id+"'");
		}
		return 200;
	}

}