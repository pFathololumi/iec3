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
			server = HttpServer.create(new InetSocketAddress(9092), 0);
			
			// add admin
			StockMarket.getInstance().addNewCustomer(new Customer("1", "admin", "admin"));

			server.createContext("/customer/add", new AddCustomer());
			server.createContext("/customer/deposit", new DepositHandler());
			server.createContext("/customer/withdraw", new WithdrawHandler());
			server.createContext("/order/sell", new SellOrder());
			server.createContext("/order/buy", new BuyOrder());
			
			server.start();
		} catch (IOException e) {
			System.out.println("Error Configuring Server");
		}
	}

}


