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
        sellingOffers=new ArrayList<>();
        buyingOffers = new ArrayList<>();
    }

    public void executeSellingByType(PrintWriter out, SellingOffer offer){
        if(offer.typeIsMatched("GTC"))
            throw new RuntimeException("No Such Method");
        else if (offer.typeIsMatched("IOC"))
            throw new RuntimeException("No Such Method");
        else if( offer.typeIsMatched("MPO"))
            throw new RuntimeException("No Such Method");
    }

    public void executeBuyingByType(PrintWriter out, BuyingOffer offer){
        if(offer.typeIsMatched("GTC"))
            throw new RuntimeException("No Such Method");
        else if (offer.typeIsMatched("IOC"))
            throw new RuntimeException("No Such Method");
        else if( offer.typeIsMatched("MPO"))
            throw new RuntimeException("No Such Method");
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
}
