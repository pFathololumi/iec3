package MyServiceHandlers;

import java.io.IOException;
import java.io.PrintWriter;

import ir.ramtung.coolserver.ServiceHandler;

public class SellOrder extends ServiceHandler{

	@Override
	public void execute(PrintWriter arg0) throws IOException {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(params.get("id"));
        String instrument = params.get("instrument");
        long price = Long.parseLong(params.get("price"));
        long quantity = Long.parseLong(params.get("quantity"));
        String type = params.get("type");
	}

}
