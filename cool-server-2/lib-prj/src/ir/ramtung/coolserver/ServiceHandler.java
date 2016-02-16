package ir.ramtung.coolserver;

import java.io.*;
import java.util.*;
import com.sun.net.httpserver.*;

public abstract class ServiceHandler implements HttpHandler {
    protected Map<String, String> params;

    private void extractParams(String queryString) {
        params = new LinkedHashMap<String, String>();
        if (queryString == null || queryString.equals(""))
            return;
        String[] paramPairs = queryString.split("\\&");
        
            for (String pp : paramPairs)
                params.put(pp.substring(0, pp.indexOf('=')), pp.substring(pp.indexOf('=') + 1));

    }

    public abstract void execute(PrintWriter out) throws IOException;

    public void handle(HttpExchange t) throws IOException {
        extractParams(t.getRequestURI().getQuery());

        StringWriter sw = new StringWriter();
        execute(new PrintWriter(sw, true /*autoflush*/));
        byte[] result = sw.toString().getBytes();
        
        t.sendResponseHeaders(200, result.length);
        Headers headers = t.getResponseHeaders();
        headers.add("Date", Calendar.getInstance().getTime().toString());
        headers.add("Content-Type", "text/html");
        OutputStream os = t.getResponseBody();
        os.write(result);
        os.close();
    }
}
