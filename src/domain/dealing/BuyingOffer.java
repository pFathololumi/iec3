package domain.dealing;

import exception.DataIllegalException;

/**
 * Created by Hamed Ara on 2/18/2016.
 */
public class BuyingOffer extends Offering {
    private String buyerID;

    public BuyingOffer(Long price, Long quantity, String type, String buyerID) {
        super(price, quantity, type);
        this.buyerID = buyerID;
    }

    @Override
    protected void validate() throws DataIllegalException {
        if(buyerID==null || buyerID.isEmpty())
            throw new DataIllegalException("Mismatched Parameters");
    }

    @Override
    public String getID() {
        return buyerID;
    }
}
