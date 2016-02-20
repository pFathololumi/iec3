package myServiceHandlers;

import java.io.IOException;
import java.io.PrintWriter;

import server.MyServiceHandler;
import server.StockMarket;
import domain.dealing.BuyingOffer;
import exception.DataIllegalException;
import ir.ramtung.coolserver.ServiceHandler;

public class BuyOrder extends MyServiceHandler {

	@Override
	public int executeByStatus(PrintWriter out) throws IOException {
		String id = params.get("id");
		String instrument = params.get("instrument");
		String type = params.get("type");
		BuyingOffer buyingOffer=null;
		try {
			Long price = Long.parseLong(params.get("price"));
			Long quantity = Long.parseLong(params.get("quantity"));
			buyingOffer = new BuyingOffer(price,quantity,type,id);
			if(instrument==null || instrument.isEmpty())
				throw new DataIllegalException("Mismatched Parameters");
			buyingOffer.validateVariables();
		} catch (DataIllegalException e) {
			out.println(e.getMessage());
			return 404;
		}catch(Exception e){
			out.println("Mismatched Parameters");
			return 404;
		}
		StockMarket.getInstance().executeBuyingOffer(out,buyingOffer,instrument);
		return 200;
	}

}
