import ServerHandler.FileHandler;

import com.sun.net.httpserver.HttpServer;

import Client.Customer;

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
			server = HttpServer.create(new InetSocketAddress(9092), 0);
			//server.createContext("/docs", new FileHandler());
			server.createContext("/customer/add", new AddCustomer());
			server.createContext("/customer/deposit", new DepositHandler());
			server.createContext("/customer/withdraw", new WithdrawHandler());
			server.start();
		} catch (IOException e) {
			System.out.println("Error Configuring Server");
		}
	}

}


