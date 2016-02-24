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
    private String symbol;
    private Long quantity;
    private List<SellingOffer> sellingOffers;
    private List<BuyingOffer> buyingOffers;

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
			gtcSell.sellingExecute(out, offer, sellingOffers, buyingOffers,symbol);
        }
        else if (offer.typeIsMatched("IOC"))
        {
			IOC iocSell = new IOC();
			iocSell.sellingExecute(out, offer, sellingOffers, buyingOffers,symbol);
        	
        }
        else if( offer.typeIsMatched("MPO"))
        {
        	MPO mpoSell = new MPO();
        	mpoSell.sellingExecute(out, offer, sellingOffers, buyingOffers,symbol);
        }
        
    }

	public void executeBuyingByType(PrintWriter out, BuyingOffer offer){
		try {
			Class t = Class.forName(offer.getType());

		}catch (ClassNotFoundException ex){
			out.println("Invalid type");
			return;
		}

		if(!(offer.typeIsMatched("GTC") || offer.typeIsMatched("IOC") || offer.typeIsMatched("MPO"))){
    		out.println("Invalid type");
    		return;
    	}

        if(offer.typeIsMatched("GTC"))
        {

        	GTC gtcBuy = new GTC();
        	gtcBuy.buyingExecute(out, offer, sellingOffers, buyingOffers,symbol);
        }
        else if (offer.typeIsMatched("IOC"))
        {
			IOC iocBuy = new IOC();
			iocBuy.buyingExecute(out, offer, sellingOffers, buyingOffers,symbol);

        }
        else if( offer.typeIsMatched("MPO"))
        {
        	MPO mpoBuy = new MPO();
        	mpoBuy.buyingExecute(out, offer, sellingOffers, buyingOffers,symbol);
        }

    }


	public static void matchingOffers(PrintWriter out,Boolean basedOnBuyerPrice,
			List<SellingOffer> sellingOffers,List<BuyingOffer>buyingOffers,String symbol){

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
	    			buyQuantity = buyingOffer.getQuantity();
	    			buyingOffers.remove(0);
	    			sellingOffer.setQuantity("delete", buyQuantity);
	    			sellingOffers.set(0, sellingOffer);
					if(sellingOffer.getQuantity()==0L){
						sellingOffers.remove(0);
					}
	    		}
	    		else{
	    			buyQuantity = sellingOffer.getQuantity();
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

    public static void sortOfferingListByPrice(List<? extends Offering> offers){
        Collections.sort(offers, new Comparator<Offering>() {
            @Override
            public int compare(Offering o1, Offering o2) {
                return o1.getPrice()>o2.getPrice()?1:-1;
            }
        });
//		return offers;
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
