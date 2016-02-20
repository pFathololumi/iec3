package domain.dealing;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import server.StockMarket;

/**
 * Created by Hamed Ara on 2/18/2016.
 */
public class Instrument {
    private String symbol;
    private Long quantity;
    private List<SellingOffer> sellingOffers;
    private List<BuyingOffer> buyingOffers;

    public Instrument(String symbol,Long quantity) {
        this.symbol = symbol;
        this.quantity = quantity;
        sellingOffers = new ArrayList<>();
        buyingOffers = new ArrayList<>();
    }

    public void executeSellingByType(PrintWriter out, SellingOffer offer){ 
    	if(!(offer.typeIsMatched("GTC") || offer.typeIsMatched("IOC") || offer.typeIsMatched("MPO"))){
    		out.println("Invalid type");
    		return;
    	}

        if(offer.typeIsMatched("GTC"))
        {
        	sellingOffers.add(offer);
        	sortSellingOfferListByPrice();
        	SellingOffer minimumOffer = sellingOffers.get(0);
        	BuyingOffer maximumOffer = buyingOffers.get(0);
        	matchingOffers(out,minimumOffer, maximumOffer);
        }
        else if (offer.typeIsMatched("IOC"))
        {
        	Long count = offer.getQuantity();
        	for (int i = 0; i < buyingOffers.size(); i++) {
				count -= buyingOffers.get(i).getQuantity();
			}
        	if(count > 0){
        		out.println("Order is declined");
        		return;
        	}
        	else{
        		count = offer.getQuantity();
        		while(count > 0){
        			if(offer.getPrice() < buyingOffers.get(0).getPrice()){
        				Long buyPrice = buyingOffers.get(0).getPrice();
        				Long buyQuantity = (long) 0 ;
        				if(buyingOffers.get(0).getQuantity() < offer.getQuantity()){
        					buyQuantity = offer.getQuantity() - buyingOffers.get(0).getQuantity();
        					buyingOffers.remove(0);
        					offer.setQuantity("delete", buyQuantity);
        					sellingOffers.set(0, offer);
        				}
        				else{
        					buyQuantity = buyingOffers.get(0).getQuantity() - offer.getQuantity();
        					buyingOffers.get(0).setQuantity("delete", buyQuantity);
        					buyingOffers.set(0, buyingOffers.get(0));
        				}
        				StockMarket.changeCustomerProperty(offer, buyingOffers.get(0), buyPrice, buyQuantity, symbol);
        				out.println(offer.getID()+" sold "+buyQuantity+" shares of "+this.symbol+" @"+buyPrice+" to "+buyingOffers.get(0).getID());
        			}
        		}
        	}
        	
        }
        else if( offer.typeIsMatched("MPO"))
        {
        	Long count = offer.getQuantity();
        	for (int i = 0; i < buyingOffers.size(); i++) {
				count -= buyingOffers.get(i).getQuantity();
			}
        	if(count > 0){
        		out.println("Order is declined");
        		return;
        	}
        	
        	sellingOffers.add(offer);
        	sortSellingOfferListByPrice();
        	SellingOffer minimumOffer = sellingOffers.get(0);
        	BuyingOffer maximumOffer = buyingOffers.get(0);
        	if(minimumOffer.getPrice() == 0)
        		minimumOffer.setPrice(maximumOffer.getPrice());
        	matchingOffers(out,minimumOffer, maximumOffer);
        }
        
    }

    public void executeBuyingByType(PrintWriter out, BuyingOffer offer){
    	if(!(offer.typeIsMatched("GTC") || offer.typeIsMatched("IOC") || offer.typeIsMatched("MPO"))){
    		out.println("Invalid type");
    		return;
    	}

        if(offer.typeIsMatched("GTC"))
        {
        	buyingOffers.add(offer);
        	sortBuyingOfferListByPrice();
        	SellingOffer minimumOffer = sellingOffers.get(0);
        	BuyingOffer maximumOffer = buyingOffers.get(0);
        	matchingOffers(out,minimumOffer, maximumOffer);
        }
        else if (offer.typeIsMatched("IOC"))
        {
        	Long count = offer.getQuantity();
        	for (int i = 0; i < sellingOffers.size(); i++) {
				count -= sellingOffers.get(i).getQuantity();
			}
        	if(count > 0){
        		out.println("Order is declined");
        		return;
        	}
        	else{
        		count = offer.getQuantity();
        		while(count > 0){
        			if(offer.getPrice() > sellingOffers.get(0).getPrice()){
        				Long buyPrice = offer.getPrice();
        				Long buyQuantity = (long) 0 ;
        				if(offer.getQuantity() < sellingOffers.get(0).getQuantity()){
        					buyQuantity = sellingOffers.get(0).getQuantity() - offer.getQuantity();
        					sellingOffers.get(0).setQuantity("delete", buyQuantity);
        					sellingOffers.set(0, sellingOffers.get(0));
        				}
        				else{
        					buyQuantity = offer.getQuantity() - sellingOffers.get(0).getQuantity();
        					sellingOffers.remove(0);
        					offer.setQuantity("delete", buyQuantity);
        					buyingOffers.set(0, offer);
        				}
        				StockMarket.changeCustomerProperty(sellingOffers.get(0), offer, buyPrice, buyQuantity, symbol);
        				out.println(offer.getID()+" sold "+buyQuantity+" shares of "+this.symbol+" @"+buyPrice+" to "+buyingOffers.get(0).getID());
        			}
        		}
        	}
        	
        }
        else if( offer.typeIsMatched("MPO"))
        {
        	Long count = offer.getQuantity();
        	for (int i = 0; i < sellingOffers.size(); i++) {
				count -= sellingOffers.get(i).getQuantity();
			}
        	if(count > 0){
        		out.println("Order is declined");
        		return;
        	}
        	
        	buyingOffers.add(offer);
        	sortBuyingOfferListByPrice();
        	SellingOffer minimumOffer = sellingOffers.get(0);
        	BuyingOffer maximumOffer = buyingOffers.get(0);
        	if(maximumOffer.getPrice() == 0)
        		maximumOffer.setPrice(minimumOffer.getPrice());
        	matchingOffers(out,minimumOffer, maximumOffer);
        }
     
    }
    
    public void matchingOffers(PrintWriter out,SellingOffer offer1,BuyingOffer offer2){  
    	if(offer1.getPrice() < offer2.getPrice()){
    		Long buyPrice = offer2.getPrice();
    		Long buyQuantity = (long) 0 ;
    		if(offer2.getQuantity() < offer1.getQuantity()){
    			buyQuantity = offer1.getQuantity() - offer2.getQuantity();
    			buyingOffers.remove(0);
    			offer1.setQuantity("delete", buyQuantity);
    			sellingOffers.set(0, offer1);
    		}
    		else{
    			buyQuantity = offer2.getQuantity() - offer1.getQuantity();
    			sellingOffers.remove(0);
    			offer2.setQuantity("delete", buyQuantity);
    			buyingOffers.set(0, offer2);
    		}
    		StockMarket.changeCustomerProperty(offer1, offer2, buyPrice, buyQuantity, symbol);
    		out.println(offer1.getID()+" sold "+buyQuantity+" shares of "+this.symbol+" @"+buyPrice+" to "+offer2.getID());
    	}
    	else
    		out.println("Order is queued");
    }

    private void sortSellingOfferListByPrice(){
        Collections.sort(sellingOffers, new Comparator<SellingOffer>() {
            @Override
            public int compare(SellingOffer o1, SellingOffer o2) {
                return o1.getPrice()>o2.getPrice()?1:-1;
            }
        });
    }

    private void sortBuyingOfferListByPrice(){
        Collections.sort(buyingOffers, new Comparator<BuyingOffer>() {
            @Override
            public int compare(BuyingOffer o1, BuyingOffer o2) {
                return o1.getPrice()<o2.getPrice()?1:-1;
            }
        });
    }

    public Boolean symbolIsMatched(String symbol){
        return this.symbol.equals(symbol);
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
