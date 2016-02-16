package ir.ramtung.coolserver;

import java.io.*;
import java.util.*;
import com.sun.net.httpserver.*;

public class Page {
    public Page(String filename) {
        this.filename = filename;
        this.attributes = new HashMap<String, String>();
    }
    public Page subst(String key, String value) {
        attributes.put("\\$"+key, value);
        return this;
    }
    public void writeTo(PrintWriter out) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = in.readLine()) != null) {
                for (String key : attributes.keySet())
                    line = line.replaceAll(key, attributes.get(key));
                out.println(line);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String filename;
    private Map<String, String> attributes;
}
