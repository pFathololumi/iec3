package myServiceHandlers;

import com.sun.net.httpserver.HttpExchange;
import server.MyServiceHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Hamed Ara on 2/24/2016.
 */
public class UploadZipFile extends MyServiceHandler {
    @Override
    public int executeByStatus(PrintWriter printWriter) throws IOException {
        return 200;
    }

    @Override
    public int executePostRequest(PrintWriter printWriter, HttpExchange t) {
        System.out.println(t);
        ZipInputStream zip= new ZipInputStream( t.getRequestBody());
        try {
            ZipEntry ze = zip.getNextEntry();
            while (ze != null) {

                String fileName = ze.getName();
                File newFile = new File(fileName);

                System.out.println("file unzip : " + newFile.getAbsoluteFile());

                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                byte[] buffer = new byte[1024];
                while ((len = zip.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zip.getNextEntry();
            }
        }catch (IOException ex){
            System.out.println("termal");
        }
        printWriter.println("bonjol");
        return 200;
    }
}
