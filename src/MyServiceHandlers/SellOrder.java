package myServiceHandlers;

import java.io.IOException;
import java.io.PrintWriter;

import server.StockMarket;
import exception.DataIllegalException;
import ir.ramtung.coolserver.ServiceHandler;
import domain.dealing.SellingOffer;

public class SellOrder extends ServiceHandler{

	@Override
	public void execute(PrintWriter out) throws IOException {
		String id = params.get("id");
        String instrument = params.get("instrument");
        Long price = Long.parseLong(params.get("price"));
        Long quantity = Long.parseLong(params.get("quantity"));
        String type = params.get("type");
        SellingOffer sellingOffer = new SellingOffer(price,quantity,type,id);
        try {
            if(instrument==null || instrument.isEmpty())
                throw new DataIllegalException("Mismatched Parameters");
            sellingOffer.validateVariables();
        } catch (DataIllegalException e) {
            out.println(e.getMessage());
        }
        StockMarket.getInstance().executeSellingOffer(out,sellingOffer,instrument);

    }

}
