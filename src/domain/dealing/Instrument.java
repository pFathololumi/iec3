package domain.dealing;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    	sellingOffers.add(offer);
    	sortSellingOfferListByPrice();
    	
    	
        if(offer.typeIsMatched("GTC"))
            throw new RuntimeException("No Such Method");
        else if (offer.typeIsMatched("IOC"))
            throw new RuntimeException("No Such Method");
        else if( offer.typeIsMatched("MPO"))
            throw new RuntimeException("No Such Method");
        else
        	out.println("Invalid type");
    }

    public void executeBuyingByType(PrintWriter out, BuyingOffer offer){
    	buyingOffers.add(offer);
    	sortBuyingOfferListByPrice();
    	
        if(offer.typeIsMatched("GTC"))
            throw new RuntimeException("No Such Method");
        else if (offer.typeIsMatched("IOC"))
            throw new RuntimeException("No Such Method");
        else if( offer.typeIsMatched("MPO"))
            throw new RuntimeException("No Such Method");
        else
        	out.println("Invalid type");
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
