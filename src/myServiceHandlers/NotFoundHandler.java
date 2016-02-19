package myServiceHandlers;

import server.MyServiceHandler;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Hamed Ara on 2/19/2016.
 */
public class NotFoundHandler extends MyServiceHandler {
    @Override
    public int executeByStatus(PrintWriter printWriter) throws IOException {
        printWriter.println("Unknown Command");
        return 404;
    }
}
