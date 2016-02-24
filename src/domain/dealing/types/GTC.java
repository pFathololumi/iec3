package domain.dealing.types;

import java.io.PrintWriter;
import java.util.List;

import domain.dealing.BuyingOffer;
import domain.dealing.Instrument;
import domain.dealing.SellingOffer;

public class GTC implements ITypeExecutor {

	@Override
	public void sellingExecute(PrintWriter out, SellingOffer offer, List<SellingOffer> sellingOffers, List<BuyingOffer> buyingOffers,String symbol) {
		sellingOffers.add(offer);
		Instrument.sortOfferingListByPrice(sellingOffers);
		if (buyingOffers.isEmpty())
			out.println("Order is queued");
		else
			Instrument.matchingOffers(out, true,sellingOffers,buyingOffers,symbol);
	}

	@Override
	public void buyingExecute(PrintWriter out, BuyingOffer offer, List<SellingOffer> sellingOffers, List<BuyingOffer> buyingOffers,String symbol) {
		buyingOffers.add(offer);
		Instrument.sortOfferingListByPrice(buyingOffers);
		if (sellingOffers.isEmpty())
			out.println("Order is queued");
		else
			Instrument.matchingOffers(out, true,sellingOffers,buyingOffers,symbol);
	}

	
}
