package domain.dealing;

import exception.DataIllegalException;

/**
 * Created by Hamed Ara on 2/18/2016.
 */
public abstract class Offering {
    private Long price;
    private Long quantity;
    private String type;

    public Offering(Long price, Long quantity, String type) {
        this.price = price;
        this.quantity = quantity;
        this.type = type;
    }

    public void validateVariables() throws DataIllegalException{
        if(price==null || quantity==null || type==null || type.isEmpty())
            throw new DataIllegalException("Mismatched Parameters");
        validate();
    }

    protected abstract void validate()throws DataIllegalException;

    public Boolean typeIsMatched(String type){
        return this.type.equals(type);
    }

    public abstract String getID();

    public Boolean isAdminOffer(){
        return getID().equals("1");
    }

    public Long getPrice() {
        return price;
    }
}
