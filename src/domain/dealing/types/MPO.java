package domain.dealing.types;

import java.io.PrintWriter;
import java.util.List;

import domain.dealing.BuyingOffer;
import domain.dealing.Instrument;
import domain.dealing.SellingOffer;

public class MPO implements ITypeExecutor {

	@Override
	public UpdatedOfferingLists sellingExecute(PrintWriter out, SellingOffer offer, List<SellingOffer> sellingOffers, List<BuyingOffer> buyingOffers,String symbol) {
		Long count = offer.getQuantity();
    	for (int i = 0; i < buyingOffers.size(); i++) {
			count -= buyingOffers.get(i).getQuantity();
		}
    	if(count > 0){
    		out.println("Order is declined");
    		return null;
    	}
		offer.setPrice(0L);
		sellingOffers.add(offer);
    	Instrument.sortOfferingListByPrice(sellingOffers);
    	Instrument.matchingOffers(out,true,sellingOffers,buyingOffers,symbol);
		return new UpdatedOfferingLists(sellingOffers,buyingOffers);
	}

	@Override
	public UpdatedOfferingLists buyingExecute(PrintWriter out, BuyingOffer offer, List<SellingOffer> sellingOffers, List<BuyingOffer> buyingOffers,String symbol) {
		Long count = offer.getQuantity();
    	for (int i = 0; i < sellingOffers.size(); i++) {
			count -= sellingOffers.get(i).getQuantity();
		}
    	if(count > 0){
    		out.println("Order is declined");
    		return null;
    	}
		offer.setPrice(Long.MAX_VALUE);
    	buyingOffers.add(offer);
    	Instrument.sortOfferingListByPrice(buyingOffers);
    	Instrument.matchingOffers(out,false,sellingOffers,buyingOffers,symbol);
		return new UpdatedOfferingLists(sellingOffers,buyingOffers);
	}

}
