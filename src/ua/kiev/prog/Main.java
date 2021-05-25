package ua.kiev.prog;

import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner (System.in);
		try{
			User user = autorizacia ();
			if (user == null){
				return;
			}
			Menu(user);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			sc.close();
		}
	}

	private static User autorizacia() throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter your login:");
		String login = sc.nextLine();
		System.out.println("Enter your password:");
		String password = sc.nextLine();

		User user = new User (login, password);// создаем новый объект юзера
		int num = user.sendcheckuser();//// передаем данные на сервер через метод sendcheckuser
		if (num != 201) {
			System.out.println("Your password and login correct!");
		} else {
			System.out.println("Plise, input correct login or passord! ");
		}
		return user;
	}

	public static void Menu(User user) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Input  1: commons chat");
		System.out.println("Input 2: personal chat");
		int a = scanner.nextInt();
		switch (a) {
			case 1:
				message(user);
				break;
			case 2:
				privateMessages(user);
				break;
			default:
				return;
		}

	}
	public static void message (User user) {
		Scanner scanner = new Scanner(System.in);

			Thread th = new Thread(new GetThread());//фоновый поток (трэд), который постоянно шлет get запрос, получает новое сообщение от сервера и выводит их на консоль
			th.setDaemon(true);
			th.start();


			System.out.println("Enter your message: ");
			while (true) {
				String text = scanner.nextLine();
				if (text.isEmpty()) break;

				Message m = new Message (user.getLogin(), text);
				int res = 0;
				try {
					res = m.send(Utils.getURL() + "/add");
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (res != 200) { // 200 OK
					System.out.println("HTTP error occured: " + res);
					return;
				}
			}
	}

	public static void privateMessages (User user) {
		Scanner scanner = new Scanner(System.in);
		try {
			System.out.println("Enter name person for privat messege: ");
			String to = scanner.nextLine();
			Thread th = new Thread(new GetThreadPrivMess());
			th.setDaemon(true);
			th.start();

			System.out.println("Enter your private message: ");//суть - читаем сообщение, создаем объект месседж, отправляем по указанному адресу
			while (true) {// бесконечный цикл
				String text = scanner.nextLine();
				if (text.isEmpty()) break;

				PrivateMessages m = new PrivateMessages(user.getLogin (), to, text);//создаем объект месседж, (тот же класс который на сервере) передаем ему отправителя и т.д.
				int res = m.send(Utils.getURL() + "/add");//m.send - отправляем наше сообщение - utils.getURL - возвращает адресс сервера и мы добвляем /add (эндпойнт эдд), int res - это возвращается статус код

				if (res != 200) { // 200 OK
					System.out.println("HTTP error occured: " + res);
					return;
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			scanner.close();
		}
	}
}
