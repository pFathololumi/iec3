package server;

import domain.Customer;
import domain.dealing.*;
import exception.DataIllegalException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hamed Ara on 2/18/2016.
 */
public class StockMarket {
    private static StockMarket stockMarket=null;
    private static List<Instrument> instruments;
    private static HashMap<String, Customer> customers;

    private StockMarket(){
        instruments = new ArrayList<>();
        customers = new HashMap<String, Customer>();
    }

    public static StockMarket getInstance(){
        if(stockMarket==null)
            stockMarket=new StockMarket();
        return stockMarket;
    }

    public void addNewCustomer(Customer newOne){
        customers.put(newOne.getId(), newOne);
    }

    public Boolean containCustomer(String id ){
        return customers.containsKey(id);
    }

    public void executeFinancialTransaction(String id, TransactionType type, Long amount){
        customers.get(id).executeTransaction(type, amount);
    }

    public Boolean customerHasEnoughMoney(String id,Long amount){
        return customers.get(id).hasEnoughMoney(amount);
    }

    public void executeSellingOffer(PrintWriter out, SellingOffer offer, String symbol){
        try {
            if(offer.isAdminOffer())
                addOrUpdateInstrumentByAdmin(out,symbol,offer);
            else{
                Instrument instrument = loadVerifiedParameters(offer,symbol);
                
                Customer customer = customers.get(offer.getID());
                if(!customer.hasEnoughStock(symbol, offer)){
                	out.println("Not enough share");
                	return;
                }
                
                instrument.executeSellingByType(out,offer);
            }

        } catch (DataIllegalException e) {
            out.println(e.getMessage());
            return;
        }
    }

    public void executeBuyingOffer(PrintWriter out, BuyingOffer offer, String symbol){
        try {
            if(offer.isAdminOffer())
                deleteOrUpdateInstrumentByAdmin(out,symbol,offer);
            else {
                Instrument instrument = loadVerifiedParameters(offer, symbol);
                
                Customer customer = customers.get(offer.getID());
                if(!customer.hasEnoughMoney(offer.getPrice() * offer.getQuantity())){
                	out.println("Not enough money");
                	return;
                }
                instrument.executeBuyingByType(out, offer);
            }
        } catch (DataIllegalException e) {
            out.println(e.getMessage());
            return;
        }
    }

    private void addOrUpdateInstrumentByAdmin(PrintWriter out,String symbol,Offering offer) {
    	boolean flag = false;
    	for(Instrument i : instruments){
			if(i.symbolIsMatched(symbol)){
				i.changeQuantity("add", offer.getQuantity());
				flag = true;
				break;
			}
		}
    	if(!flag)
    		instruments.add(new Instrument(symbol, offer.getQuantity()));
    }
    private void deleteOrUpdateInstrumentByAdmin(PrintWriter out,String symbol,Offering offer) {
    	for(Instrument i : instruments){
			if(i.symbolIsMatched(symbol)){
				i.changeQuantity("delete", offer.getQuantity());
				return;
			}
		}
    }

    private Instrument loadVerifiedParameters(Offering offer, String symbol) throws DataIllegalException {
        Instrument instrument=null;
        if(!customerIsRegistered(offer))
            throw new DataIllegalException("Unknown user id");
        if((instrument= getSymbol(symbol))==null)
            throw new DataIllegalException("Invalid symbol id");
        
        return instrument;
    }


    private Boolean customerIsRegistered(Offering offer){
        if( customers.get(offer.getID())==null)
            return false;
        else
            return true;
    }

    private Instrument getSymbol(String inst){
        for(Instrument instrument : instruments)
            if( instrument.symbolIsMatched(inst))
                return instrument;
        return null;
    }
    
    public static void changeCustomerProperty(SellingOffer sOffer,BuyingOffer bOffer,Long price,Long count,String symbol){
    	Customer seller = customers.get(sOffer.getID());
    	Customer buyer = customers.get(bOffer.getID());
    	
    	seller.executeTransaction(TransactionType.DEPOSIT, price*count);
    	seller.updateInstruments("delete", count, symbol);
    	
    	buyer.executeTransaction(TransactionType.WITHDRAW, price*count);
    	boolean flag = false;
    	for(Instrument i : instruments){
			if(i.symbolIsMatched(symbol)){
				i.changeQuantity("add", bOffer.getQuantity());
				flag = true;
				break;
			}
		}
    	if(!flag)
    		instruments.add(new Instrument(symbol, bOffer.getQuantity()));
    }
}
