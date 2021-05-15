package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GetThreadPrivMess implements Runnable {
    private final Gson gson;
    private int n;

    public GetThreadPrivMess() {
        gson = new GsonBuilder().create();
    }


    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                URL url = new URL(Utils.getURL() + "/get?from=" + n);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                InputStream is = http.getInputStream();
                try {
                    byte[] buf = responsBodyToArray(is);
                    String strBuf = new String(buf, StandardCharsets.UTF_8);
                    JsonPrivateMessages list = gson.fromJson(strBuf, JsonPrivateMessages.class);
                    if (list != null) {
                        for (Message m : list.getList()) {
                            System.out.println(m);
                            n++;
                        }
                    }

                } finally {
                   is.close();
                }

                Thread.sleep(500);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private byte[] responsBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte [] buf = new byte [10240];
        int r;
        do {
            r = is.read(buf);
            if (r> 0) bos.write(buf, 0, r);
        } while (r != -1);
        return bos.toByteArray();
    }
}
