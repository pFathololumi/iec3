package domain.dealing.types;

import java.io.PrintWriter;
import java.util.List;

import domain.dealing.BuyingOffer;
import domain.dealing.SellingOffer;

public interface TypeExecutor {
	
	public void sellingExecute(PrintWriter out, SellingOffer offer, List<SellingOffer> sellingOffers, List<BuyingOffer> buyingOffers);
	public void buyingExecute(PrintWriter out, BuyingOffer offer, List<SellingOffer> sellingOffers, List<BuyingOffer> buyingOffers);
	
}
