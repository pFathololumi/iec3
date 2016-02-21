package domain.dealing;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import logger.MyLogger;
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
        	
        	if(buyingOffers.isEmpty()){
        		out.println("Order is queued");
        		return;
        	}
        	matchingOffers(out,true);
        }
        else if (offer.typeIsMatched("IOC"))
        {
			if(buyingOffers.isEmpty()){
				out.println("Order is declined");
				return;
			}
			sellingIOCtype(out, offer);
        	
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
			offer.setPrice(0L);
			sellingOffers.add(offer);
        	sortSellingOfferListByPrice();
        	matchingOffers(out,true);
        }
        
    }

	private void sellingIOCtype(PrintWriter out, SellingOffer offer) {
		Long count = offer.getQuantity();
		for (int i = 0; i < buyingOffers.size(); i++) {
			if(buyingOffers.get(i).getPrice()<offer.getPrice())
				break;
            count -= buyingOffers.get(i).getQuantity();
        }
		if(count > 0){
            out.println("Order is declined");
            return;
        }
        else{
            while(true){
				Long buyPrice = buyingOffers.get(0).getPrice();
				Long buyQuantity = 0L ;
				if(buyingOffers.get(0).getQuantity() <= offer.getQuantity()){
					buyQuantity = offer.getQuantity() - buyingOffers.get(0).getQuantity();
					StockMarket.changeCustomerProperty(offer, buyingOffers.get(0), buyPrice, buyQuantity, symbol);
					out.println(offer.getID()+" sold "+buyQuantity+" shares of "+this.symbol+" @"+buyPrice+" to "+buyingOffers.get(0).getID());
					buyingOffers.remove(0);
					offer.setQuantity("delete", buyQuantity);
//					sellingOffers.set(0, offer);
				}
				else{
					buyQuantity = buyingOffers.get(0).getQuantity() - offer.getQuantity();
					buyingOffers.get(0).setQuantity("delete", buyQuantity);
//					buyingOffers.set(0, buyingOffers.get(0));
					StockMarket.changeCustomerProperty(offer, buyingOffers.get(0), buyPrice, buyQuantity, symbol);
					out.println(offer.getID()+" sold "+buyQuantity+" shares of "+this.symbol+" @"+buyPrice+" to "+buyingOffers.get(0).getID());
					break;
				}


            }
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
        	if(sellingOffers.isEmpty()){
        		out.println("Order is queued");
        		return;
        	}

        	matchingOffers(out,true);
        }
        else if (offer.typeIsMatched("IOC"))
        {
			if(sellingOffers.isEmpty()){
				out.println("Order is declined");
				return;
			}
			buyingIOCtype(out, offer);

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
			offer.setPrice(Long.MAX_VALUE);
        	buyingOffers.add(offer);
        	sortBuyingOfferListByPrice();
        	matchingOffers(out,false);
        }

    }

	private void buyingIOCtype(PrintWriter out, BuyingOffer offer) {
		Long count = offer.getQuantity();
		for (int i = 0; i < sellingOffers.size(); i++) {
			if(sellingOffers.get(i).getPrice()>offer.getPrice())
				break;
            count -= sellingOffers.get(i).getQuantity();
        }
		if(count > 0){
            out.println("Order is declined");
            return;
        }
        else{
//            count = offer.getQuantity();
            while(true){
                if(offer.getPrice() > sellingOffers.get(0).getPrice()){
                    Long buyPrice = offer.getPrice();
                    Long buyQuantity = 0L ;
                    if(offer.getQuantity() < sellingOffers.get(0).getQuantity()){
                        buyQuantity = sellingOffers.get(0).getQuantity() - offer.getQuantity();
                        sellingOffers.get(0).setQuantity("delete", buyQuantity);
//                        sellingOffers.set(0, sellingOffers.get(0));
						StockMarket.changeCustomerProperty(sellingOffers.get(0), offer, buyPrice, buyQuantity, symbol);
						out.println(sellingOffers.get(0).getID()+" sold "+buyQuantity+" shares of "+this.symbol+" @"+buyPrice+" to "+offer.getID());
						break;
                    }
                    else{
                        buyQuantity = offer.getQuantity() - sellingOffers.get(0).getQuantity();
						StockMarket.changeCustomerProperty(sellingOffers.get(0), offer, buyPrice, buyQuantity, symbol);
						out.println(sellingOffers.get(0).getID()+" sold "+buyQuantity+" shares of "+this.symbol+" @"+buyPrice+" to "+offer.getID());
						sellingOffers.remove(0);
                        offer.setQuantity("delete", buyQuantity);
//                        buyingOffers.set(0, offer);

                    }

                }
            }
        }
	}

	public void matchingOffers(PrintWriter out,Boolean basedOnBuyerPrice){
    	
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
	    		out.println(sellingOffer.getID()+" sold "+buyQuantity+" shares of "+this.symbol+" @"+buyPrice+" to "+buyingOffer.getID());
	    	}else
				break;
	    	if(!sellingOffers.isEmpty()&&!buyingOffers.isEmpty()) {
				sellingOffer = sellingOffers.get(0);
				buyingOffer = buyingOffers.get(0);
			}else
				break;
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
