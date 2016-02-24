package domain.dealing.types;

import domain.dealing.BuyingOffer;
import domain.dealing.SellingOffer;

import java.util.List;

/**
 * Created by Hamed Ara on 2/24/2016.
 */
public class UpdatedOfferingLists {
    public List<SellingOffer> sellingOffers;
    public List<BuyingOffer> buyingOffers;

    public UpdatedOfferingLists(List<SellingOffer> sellingOffers, List<BuyingOffer> buyingOffers) {
        this.sellingOffers = sellingOffers;
        this.buyingOffers = buyingOffers;
    }
}
