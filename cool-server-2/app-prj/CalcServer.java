import java.io.*;
import java.net.*;
import java.util.*;
import com.sun.net.httpserver.*;
import ir.ramtung.coolserver.*;

class Adder extends ServiceHandler {
    public void execute(PrintWriter out) {
        int n1 = Integer.parseInt(params.get("n1"));
        int n2 = Integer.parseInt(params.get("n2"));
        int sum = n1 + n2;
        Page responsePage = new Page("./calc.html")
            .subst("n1", params.get("n1"))
            .subst("n2", params.get("n2"))
            .subst("op", "+")
            .subst("result", sum + "");

        responsePage.writeTo(out);
    }
}

class Subtractor extends ServiceHandler {
    public void execute(PrintWriter out) {
        int n1 = Integer.parseInt(params.get("n1"));
        int n2 = Integer.parseInt(params.get("n2"));
        new Page("./calc.html")
            .subst("n1", params.get("n1"))
            .subst("n2", params.get("n2"))
            .subst("op", "-")
            .subst("result", (n1 - n2) + "")
            .writeTo(out);
    }
}

public class CalcServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);
        server.createContext("/docs", new FileHandler());
        server.createContext("/add", new Adder());
        server.createContext("/sub", new Subtractor());
        server.start();
    }
}