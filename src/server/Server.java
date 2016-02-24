package server;
import com.sun.net.httpserver.HttpServer;

import domain.Customer;
import myServiceHandlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HttpServer server = null;
		try {
			server = HttpServer.create(new InetSocketAddress(9091), 0);
			
			// add admin
			StockMarket.getInstance().addNewCustomer(new Customer("1", "admin", "admin"));

			server.createContext("/customer/add", new AddCustomer());
			server.createContext("/customer/deposit", new DepositHandler());
			server.createContext("/customer/withdraw", new WithdrawHandler());
			server.createContext("/order/sell", new SellOrder());
			server.createContext("/order/buy", new BuyOrder());
			server.createContext("/config/uploadzip", new UploadZipFile());
			server.createContext("/",new NotFoundHandler());

			server.start();
			System.out.println("Server is started.");
		} catch (IOException e) {
			System.out.println("Error Configuring Server");
		}
	}

}


