package domain.dealing.types;

import java.io.PrintWriter;
import java.util.List;

import domain.dealing.BuyingOffer;
import domain.dealing.Instrument;
import domain.dealing.SellingOffer;

public class GTC implements TypeExecutor{

	@Override
	public void sellingExecute(PrintWriter out, SellingOffer offer, List<SellingOffer> sellingOffers, List<BuyingOffer> buyingOffers) {
		// TODO Auto-generated method stub
		sellingOffers.add(offer);
    	Instrument.sortSellingOfferListByPrice();
    	
    	if(buyingOffers.isEmpty()){
    		out.println("Order is queued");
    		return;
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
