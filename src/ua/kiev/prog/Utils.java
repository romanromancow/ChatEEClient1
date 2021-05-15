package ua.kiev.prog;

public class Utils {// класс содержит две константы
    private static final String URL = "http://127.0.0.1";
    private static final int PORT = 8080;

    public static String getURL() {// возвращает конкатенацию
        return URL + ":" + PORT;
    }
}
