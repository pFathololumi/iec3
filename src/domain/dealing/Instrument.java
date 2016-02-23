package domain.dealing;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import domain.dealing.types.*;
import server.StockMarket;

/**
 * Created by Hamed Ara on 2/18/2016.
 */
public class Instrument {
    private static String symbol;
    private Long quantity;
    private static List<SellingOffer> sellingOffers;
    private static List<BuyingOffer> buyingOffers;

    @SuppressWarnings("static-access")
	public Instrument(String symbol,Long quantity) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.sellingOffers = new ArrayList<>();
        this.buyingOffers = new ArrayList<>();
    }

    public void executeSellingByType(PrintWriter out, SellingOffer offer){ 
    	if(!(offer.typeIsMatched("GTC") || offer.typeIsMatched("IOC") || offer.typeIsMatched("MPO"))){
    		out.println("Invalid type");
    		return;
    	}

        if(offer.typeIsMatched("GTC"))
        {
        	GTC gtcSell = new GTC();
        	gtcSell.sellingExecute(out, offer, sellingOffers, buyingOffers);
        }
        else if (offer.typeIsMatched("IOC"))
        {
			IOC iocSell = new IOC(symbol);
			iocSell.sellingExecute(out, offer, sellingOffers, buyingOffers);
        	
        }
        else if( offer.typeIsMatched("MPO"))
        {
        	MPO mpoSell = new MPO();
        	mpoSell.sellingExecute(out, offer, sellingOffers, buyingOffers);
        }
        
    }

	public void executeBuyingByType(PrintWriter out, BuyingOffer offer){
    	if(!(offer.typeIsMatched("GTC") || offer.typeIsMatched("IOC") || offer.typeIsMatched("MPO"))){
    		out.println("Invalid type");
    		return;
    	}

        if(offer.typeIsMatched("GTC"))
        {
        	GTC gtcBuy = new GTC();
        	gtcBuy.buyingExecute(out, offer, sellingOffers, buyingOffers);
        }
        else if (offer.typeIsMatched("IOC"))
        {
			IOC iocBuy = new IOC(symbol);
			iocBuy.buyingExecute(out, offer, sellingOffers, buyingOffers);

        }
        else if( offer.typeIsMatched("MPO"))
        {
        	MPO mpoBuy = new MPO();
        	mpoBuy.buyingExecute(out, offer, sellingOffers, buyingOffers);
        }

    }
	

	public static void matchingOffers(PrintWriter out,Boolean basedOnBuyerPrice){
    	
    	SellingOffer sellingOffer = sellingOffers.get(0);
    	BuyingOffer buyingOffer = buyingOffers.get(0);
    	
    	if(sellingOffer.getPrice() > buyingOffer.getPrice()){
    		out.println("Order is queued");
    		return;
    	}
    	
    	while(true){	
	    	if(sellingOffer.getPrice() <= buyingOffer.getPrice()){
	    		Long buyPrice = basedOnBuyerPrice? buyingOffer.getPrice():sellingOffer.getPrice();
	    		Long buyQuantity = (long) 0 ;
	    		if(buyingOffer.getQuantity() < sellingOffer.getQuantity()){
	    			buyQuantity = sellingOffer.getQuantity() - buyingOffer.getQuantity();
	    			buyingOffers.remove(0);
	    			sellingOffer.setQuantity("delete", buyQuantity);
	    			sellingOffers.set(0, sellingOffer);
					if(sellingOffer.getQuantity()==0L){
						sellingOffers.remove(0);
					}
	    		}
	    		else{
	    			buyQuantity = buyingOffer.getQuantity() - sellingOffer.getQuantity();
	    			sellingOffers.remove(0);
	    			buyingOffer.setQuantity("delete", buyQuantity);
	    			buyingOffers.set(0, buyingOffer);
					if(buyingOffer.getQuantity()==0L){
						buyingOffers.remove(0);
					}
	    		}
	    		StockMarket.changeCustomerProperty(sellingOffer, buyingOffer, buyPrice, buyQuantity, symbol);
	    		out.println(sellingOffer.getID()+" sold "+buyQuantity+" shares of "+symbol+" @"+buyPrice+" to "+buyingOffer.getID());
	    	}else
				break;
	    	if(!sellingOffers.isEmpty()&&!buyingOffers.isEmpty()) {
				sellingOffer = sellingOffers.get(0);
				buyingOffer = buyingOffers.get(0);
			}else
				break;
    	}
    }

    public static void sortSellingOfferListByPrice(){
        Collections.sort(sellingOffers, new Comparator<SellingOffer>() {
            @Override
            public int compare(SellingOffer o1, SellingOffer o2) {
                return o1.getPrice()>o2.getPrice()?1:-1;
            }
        });
    }

    public static void sortBuyingOfferListByPrice(){
        Collections.sort(buyingOffers, new Comparator<BuyingOffer>() {
            @Override
            public int compare(BuyingOffer o1, BuyingOffer o2) {
                return o1.getPrice() < o2.getPrice() ? 1 : -1;
            }
        });
    }

    public Boolean symbolIsMatched(String symbol){
        return symbol.equals(symbol);
    }
    
    public Boolean HasQuantity(Long count){
    	if(count <= this.quantity)
    		return true;
    	return false;
    }
    
    public void changeQuantity(String type,Long count){
    	if(type.equals("add"))
    		this.quantity += count;
    	else if(type.equals("delete") && HasQuantity(count))
    		this.quantity -= count;
    }
    
}
