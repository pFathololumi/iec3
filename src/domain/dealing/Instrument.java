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
    	
    	sellingOffers.add(offer);
    	sortSellingOfferListByPrice();
    	SellingOffer minimumOffer = sellingOffers.get(0);
    	BuyingOffer maximumOffer = buyingOffers.get(0);
    	matchingOffers(minimumOffer, maximumOffer);
    	
    	
        if(offer.typeIsMatched("GTC"))
            throw new RuntimeException("No Such Method");
        else if (offer.typeIsMatched("IOC"))
            throw new RuntimeException("No Such Method");
        else if( offer.typeIsMatched("MPO"))
            throw new RuntimeException("No Such Method");
        
    }

    public void executeBuyingByType(PrintWriter out, BuyingOffer offer){
    	if(!(offer.typeIsMatched("GTC") || offer.typeIsMatched("IOC") || offer.typeIsMatched("MPO"))){
    		out.println("Invalid type");
    		return;
    	}
    	
    	buyingOffers.add(offer);
    	sortBuyingOfferListByPrice();
    	SellingOffer minimumOffer = sellingOffers.get(0);
    	BuyingOffer maximumOffer = buyingOffers.get(0);
    	matchingOffers(minimumOffer, maximumOffer);
    	
        if(offer.typeIsMatched("GTC"))
            throw new RuntimeException("No Such Method");
        else if (offer.typeIsMatched("IOC"))
            throw new RuntimeException("No Such Method");
        else if( offer.typeIsMatched("MPO"))
            throw new RuntimeException("No Such Method");
     
    }
    
    public void matchingOffers(SellingOffer offer1,BuyingOffer offer2){  
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
    	}
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
