package ir.ramtung.coolserver;

import java.io.*;
import java.util.*;
import com.sun.net.httpserver.*;

public class FileHandler implements HttpHandler {
    public static String contentType(String path) {
        if (path.endsWith(".html") || path.endsWith(".txt")) 
            return "text/html";
        if (path.endsWith(".png"))
            return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg"))
            return "image/jpeg";
        return "application/octet-stream";
    }
 
    public void handle(HttpExchange t) throws IOException {
    	System.out.println(t.getRequestURI().getPath());
        File file = new File("." + t.getRequestURI().getPath());
        if (!file.exists()) {
            t.sendResponseHeaders(404, -1);
            return;
        }    	
        t.sendResponseHeaders(200, file.length());
        Headers headers = t.getResponseHeaders();
        headers.add("Date", Calendar.getInstance().getTime().toString());
        headers.add("Content-Type", contentType(t.getRequestURI().getPath()));
        OutputStream os = t.getResponseBody();
        BufferedInputStream payloadFile = new BufferedInputStream(new FileInputStream(file));
        int b;
        while ((b = payloadFile.read()) != -1)
        	os.write(b);
        payloadFile.close();
        os.close();
    }
}
