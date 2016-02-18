package Server;
import com.sun.net.httpserver.HttpServer;

import Client.Customer;
import MyServiceHandlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class Server {

	public static HashMap<Integer, Customer> customers;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		customers = new HashMap<Integer, Customer>();
		
		HttpServer server = null;
		try {
			server = HttpServer.create(new InetSocketAddress(9091), 0);
			
			// add admin
			Server.customers.put(1, new Customer(1, "admin", "admin"));
			
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


