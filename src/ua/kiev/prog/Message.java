package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Message {
	private Date date = new Date();
	private String from;
	private String to;
	private String text;

	public Message(String from, String text, String to) {
		this.to = to;
		this.text = text;
		this.from = from;
	}


	public String toJSON() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}
	
	public static Message fromJSON(String s) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(s, Message.class);
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append("[").append(date)
				.append(", From: ").append(from).append(", To: ").append(to)
				.append("] ").append(text)
                .toString();
	}

	public int send(String url) throws IOException {//метод send отправляет сообщения по указанному URLu
		URL obj = new URL(url);//заворачиваем строку с адресом в объект URL
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();//используем стандартный библиотечный класс HttpURLConnection и вызываем openConnection
		
		conn.setRequestMethod("POST");//указываем тип метода  - POST
		conn.setDoOutput(true);// Output(true)  - указывам что будут данные

		try (OutputStream os = conn.getOutputStream()) {// получаем OutputStream, все что мы в OutputStream напишем улетает post запросом на сервер
			String json = toJSON();
			os.write(json.getBytes(StandardCharsets.UTF_8));
			return conn.getResponseCode();// getResponseCode вернет статус код который вернул серверу
		}
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
