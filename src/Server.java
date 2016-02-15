import ServerHandler.FileHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HttpServer server = null;
		try {
			server = HttpServer.create(new InetSocketAddress(9090), 0);
			server.createContext("/docs", new FileHandler());

			server.start();
		} catch (IOException e) {
			System.out.println("Error Configuring Server");
		}
	}

}
