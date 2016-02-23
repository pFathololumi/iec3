package domain.dealing.types;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;import com.sun.xml.internal.ws.handler.ServerLogicalHandlerTube;

import domain.dealing.BuyingOffer;
import domain.dealing.Instrument;
import domain.dealing.Offering;
import domain.dealing.SellingOffer;

public class GTC implements TypeExecutor{

	@Override
	public List<List<Offering>> sellingExecute(PrintWriter out, SellingOffer offer, List<SellingOffer> sellingOffers, List<BuyingOffer> buyingOffers) {
		// TODO Auto-generated method stub
		List<List<Offering>> offerings = new ArrayList<>();
		
		sellingOffers.add(offer);
    	Instrument.sortSellingOfferListByPrice();
    	offerings.add(sellingOffers);
    	offerings.add(buyingOffers);
    	
    	if(buyingOffers.isEmpty()){
    		out.println("Order is queued");
    		return offering;
    	}
    	Instrument.matchingOffers(out,true);
    	
    	
	}

	@Override
	public void buyingExecute(PrintWriter out, BuyingOffer offer, List<SellingOffer> sellingOffers, List<BuyingOffer> buyingOffers) {
		// TODO Auto-generated method stub
		buyingOffers.add(offer);
		Instrument.sortBuyingOfferListByPrice();
    	if(sellingOffers.isEmpty()){
    		out.println("Order is queued");
    		return;
    	}

    	Instrument.matchingOffers(out,true);
		
	}

	
}
