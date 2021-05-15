package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GetThread implements Runnable {
    private final Gson gson;
    private int n;// в поле n лежит счетчик для from запросов

    public GetThread() {
        gson = new GsonBuilder().create();
    }

    @Override
    public void run() {// метод run
        try {
            while ( ! Thread.interrupted()) {// пока трэд не прервали
                URL url = new URL(Utils.getURL() + "/get?from=" + n);// делаем запрос по адресу /get?from + n. n будет по мере работы увеличиваться
                HttpURLConnection http = (HttpURLConnection) url.openConnection();// openConnection делаем запрос по вот этому адресу (по какому?)

                InputStream is = http.getInputStream();//getInputStream()- получаем стрим из которого вычитываем ответ
                try {
                    byte[] buf = responseBodyToArray(is);// как вычитываем - есть метод responseBodyToArray (читам из стрима пока читается)- вычитывам все содержимое в массив
                    String strBuf = new String(buf, StandardCharsets.UTF_8);// массив преобразуем в строку

                    JsonMessages list = gson.fromJson(strBuf, JsonMessages.class);// из строки пытаемся восстановть объект jsonMessages
                    if (list != null) {
                        for (Message m : list.getList()) {// если получилось, внутри там будет лежать лист объектов месседж  и мы по нему запускаем for each
                            System.out.println(m);// каждое сообщение выводим на консоль
                            n++;// n с каждым разом увеличивается на единицу
                        }
                    }
                } finally {
                    is.close();// закрывам стрим
                }

                Thread.sleep(500);// задержка тред слип на пол секунды
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private byte[] responseBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }
}
