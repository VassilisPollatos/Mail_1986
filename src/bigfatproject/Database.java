package bigfatproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Database {
	static Connection connection;
	DriverManager dm;
	static Statement stm;
	static ArrayList<User> users = new ArrayList<User>();

	public Database() {

	}

	public static Connection connect(String _DB_URL, String _username, String _password) {
		try {
			connection = DriverManager.getConnection(_DB_URL, _username, _password);
			return connection;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}
	}

	public int executeStatement(String sql) {
		try {
			stm = connection.createStatement();
			return stm.executeUpdate(sql);
		} catch (SQLException e) {

			e.printStackTrace();
			return -22;
		}
	}

	public static void viewInbox(String username) {
		try {
			String query = ("select data,sender,date from messages,users where messages.receiver=users.userid and users.username="
					+ "'" + username + "'");

			stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				String messages = rs.getString("data");
				String sender = rs.getString("sender");
				String date = rs.getString("date");

				System.out.println("Message : " + "[" + messages + "]");
				System.out.println("Sent from : " + "[" + getUsername(sender) + "]");
				System.out.println("Date :" + "[" + date + "]" + "\n");
				Write.write("view inbox", username);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public static String getUsername(String id) {
		try {
			String query = ("select username from users where users.userid= " + "'" + id + "'");

			stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				String username = rs.getString("username");

				return username;

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;

	}

	public static void viewOutbox(String username) {
		try {
			String query = ("select data,receiver,date from messages,users where messages.sender=users.userid and users.username="
					+ "'" + username + "'");

			stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				String messages = rs.getString("data");
				String receiver = rs.getString("receiver");
				String date = rs.getString("date");

				System.out.println("Message : " + "[" + messages + "]");
				System.out.println("Sent to : " + "[" + getUsername(receiver) + "]");
				System.out.println("Date :" + "[" + date + "]" + "\n");
				Write.write("view outbox", username);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public static void login() {
		User user1 = null;
		System.out.println("Enter username");
		Scanner sca = new Scanner(System.in);
		String userName = sca.next();
		System.out.println("Enter password");
		Scanner scan = new Scanner(System.in);
		String passWord = scan.next();
		if (userName.equals("admin") && passWord.equals("admin")) {
			Write.write("login", "admin");
			MenuAdmin();

		} else {
			ArrayList<User> users = getUser();

			boolean found = false;

			for (User user : users) {
				if (user.getUsername().equals(userName) && user.getPassword().equals(passWord)) {
					found = true;
					user1 = user;

					break;

				}

			}
			if (found) {
				System.out.println("Succesful log in");
				Write.write("login", user1.getUsername());
				Menus(user1);

			} else {
				System.out.println("Invalid username or password");
				login();
			}
		}
	}

	public static void sendMessage(String username) {

		try {
			System.out.println("Send a message to one of the following users");

			for (User user : users) {
				System.out.println(user.username);
			}
			Scanner sca = new Scanner(System.in);
			String sendto = sca.next();
			if (exists(sendto)) {
				System.out.println("Type your message to " + sendto + " and press enter");
				Scanner scan = new Scanner(System.in);
				String data = scan.nextLine();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime dateTime = LocalDateTime.now();
				String date = dateTime.format(formatter);
				String query2 = ("insert into messages values(null,?,?,?,?)");
				PreparedStatement stm = connection.prepareStatement(query2);

				stm.setInt(1, getId(username));
				stm.setInt(2, getId(sendto));
				stm.setString(3, data);
				stm.setString(4, date);
				stm.executeUpdate();
				System.out.println("Message succesfully sent");
				Write.writeMessage(username, sendto, data);

			} else {
				System.out.println("User does not exist");
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public static int getId(String username) {
		try {
			String query = ("select userid from users where users.username= " + "'" + username + "'");

			stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				int id = rs.getInt("userid");

				return id;

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return 0;

	}

	public static ArrayList<User> getUser() {

		String query = ("select * from users");
		users = new ArrayList<User>();

		try {
			stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				int id = rs.getInt("userid");
				String username = rs.getString("username");
				String password = rs.getString("password");
				String role = rs.getString("role");

				if (role.equals("R")) {
					users.add(new User(id, username, password, role));
				} else if (role.equals("RE")) {
					users.add(new User2(id, username, password, role));
				} else if (role.equals("RED")) {
					users.add(new User3(id, username, password, role));

				}
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return users;

	}

	public static void MainMenu() {
		String url = "jdbc:mysql://localhost:3306/project?useSSL=false";

		Database.connect(url, "root", "6230Sql");
		System.out.println("Welcome to Mail_1986!");
		System.out.println("(With new bugs never seen before!)" + "\n");
		Database.login();

	}

	public static void menuR(String username) {

		boolean go = false;

		do {
			// System.out.println("\n" + "Student List");
			System.out.println();
			System.out.println("Press 1 to view inbox");
			System.out.println("Press 2 to view outbox");
			System.out.println("Press 3 to send a message");
			System.out.println("Press 4 to exit application");

			@SuppressWarnings("resource")
			Scanner sca = new Scanner(System.in);
			if (sca.hasNextInt()) {
				int input = sca.nextInt();

				switch (input) {
				case 1:
					viewInbox(username);
					break;
				case 2:
					viewOutbox(username);
					break;
				case 3:
					sendMessage(username);
					break;
				case 4:
					System.out.println("Goodbye!");
					Write.write("logout", username);
					go = true;
					
					break;
				default:
					System.out.println("Please enter a valid command" + "\n");
					go = false;

				}
			}

		} while (!go);
	}

	public static void menuRe(String username) {

		boolean go = false;

		do {

			System.out.println();
			System.out.println("Press 1 to view inbox");
			System.out.println("Press 2 to view outbox");
			System.out.println("Press 3 to send a message");
			System.out.println("Press 4 to change username");
			System.out.println("Press 5 to change password");
			System.out.println("Press 6 to exit application");
			@SuppressWarnings("resource")
			Scanner sca = new Scanner(System.in);
			if (sca.hasNextInt()) {
				int input = sca.nextInt();

				switch (input) {
				case 1:
					viewInbox(username);
					break;
				case 2:
					viewOutbox(username);
					break;
				case 3:
					sendMessage(username);
					break;
				case 4:
					editUsername(username);
					break;
				case 5:
					editPassword(username);
					break;

				case 6:
					System.out.println("Goodbye!");
					Write.write("logout", username);
					go = true;
					break;
				default:
					System.out.println("Please enter a valid command" + "\n");
					go = false;

				}
			}

		} while (!go);
	}

	public static void menuRed(String username) {

		boolean go = false;

		do {

			System.out.println();
			System.out.println("Press 1 to view inbox");
			System.out.println("Press 2 to view outbox");
			System.out.println("Press 3 to send a message");
			System.out.println("Press 4 to change username");
			System.out.println("Press 5 to change password");
			System.out.println("Press 6 to delete from inbox");
			System.out.println("Press 7 to delete from outbox");
			System.out.println("Press 8 to exit application");
			@SuppressWarnings("resource")
			Scanner sca = new Scanner(System.in);
			if (sca.hasNextInt()) {
				int input = sca.nextInt();

				switch (input) {
				case 1:
					viewInbox(username);
					break;
				case 2:
					viewOutbox(username);
					break;
				case 3:
					sendMessage(username);
					break;
				case 4:
					editUsername(username);
					break;
				case 5:
					editPassword(username);
					break;
				case 6:
					deleteFromInbox(username);
					break;
				case 7:

					deleteFromOutbox(username);
					break;
				case 8:
					System.out.println("Goodbye!");
					Write.write("logout", username);
					go = true;
					break;
				default:
					System.out.println("Please enter a valid command" + "\n");
					go = false;

				}
			}

		} while (!go);
	}

	public static void MenuAdmin() {
		boolean go = false;

		do {

			System.out.println();
			System.out.println("Press 1 to create a user");
			System.out.println("Press 2 to edit a user role");
			System.out.println("Press 3 to delete a user");
			System.out.println("Press 4 to exit ");

			@SuppressWarnings("resource")
			Scanner sca = new Scanner(System.in);
			if (sca.hasNextInt()) {
				int input = sca.nextInt();

				switch (input) {
				case 1:
					createUser();
					break;
				case 2:
					editUserRole();
					break;
				case 3:
					deleteUser();
					break;

				case 4:
					System.out.println("Goodbye!");
					Write.write("logout", "admin");
					go = true;
					break;
				default:
					System.out.println("Please enter a valid command" + "\n");
					go = false;

				}
			}

		} while (!go);
	}

	public static void Menus(User user1) {

		boolean go = false;

		do {

			switch (user1.getRole()) {
			case "R":
				menuR(user1.username);
				go = true;
				break;
			case "RE":
				menuRe(user1.username);
				go = true;
				break;
			case "RED":
				menuRed(user1.username);
				go = true;
				break;

			default:
				System.out.println("Please enter a valid command" + "\n");
				go = false;

			}

		} while (!go);
	}

	public static void editUsername(String username) {
		try {
			System.out.println("Type your new username");

			Scanner sca = new Scanner(System.in);
			String userName = sca.next();
			String query2 = ("update users set username= ? where userid= ?");
			PreparedStatement stm = connection.prepareStatement(query2);

			stm.setString(1, userName);
			stm.setInt(2, getId(username));

			stm.executeUpdate();
			System.out.println("Username has changed");
			Write.write("changed username", "from " + username + " to " + userName);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public static void editPassword(String username) {
		try {
			System.out.println("Type your new password");

			Scanner sca = new Scanner(System.in);
			String passWord = sca.next();
			String query2 = ("update users set password= ? where userid= ?");
			PreparedStatement stm = connection.prepareStatement(query2);

			stm.setString(1, passWord);
			stm.setInt(2, getId(username));

			stm.executeUpdate();
			System.out.println("Password has changed");
			Write.write("changed password ", " to " + passWord);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public static void deleteFromInbox(String username) {
		try {
			String query = ("select mid,data,sender,date from messages,users where messages.receiver=users.userid and users.username="
					+ "'" + username + "'");

			stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				int mId = rs.getInt("mid");
				String messages = rs.getString("data");
				String sender = rs.getString("sender");
				String date = rs.getString("date");
				System.out.println("Message id : " + "[" + mId + "]");
				System.out.println("Messages : " + "[" + messages + "]");
				System.out.println("Sent from : " + "[" + getUsername(sender) + "]");
				System.out.println("Date :" + "[" + date + "]" + "\n");
			}
			System.out.println("\n" + "Type the message id of the message you want to delete");

			Scanner sca = new Scanner(System.in);
			if (sca.hasNextInt()) {
				int mid = sca.nextInt();

				String query2 = ("delete from  messages  where mid= ?");
				PreparedStatement stm = connection.prepareStatement(query2);

				stm.setInt(1, mid);

				stm.executeUpdate();
				System.out.println("Message succesfully deleted");
				Write.write("deleted an inbox message", username);
			} else {
				System.out.println("Wrong message id");
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public static void deleteFromOutbox(String username) {

		try {
			String query = ("select mid,data,receiver,date from messages,users where messages.sender=users.userid and users.username="
					+ "'" + username + "'");

			stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				int mId = rs.getInt("mid");
				String messages = rs.getString("data");
				String receiver = rs.getString("receiver");
				String date = rs.getString("date");
				System.out.println("Message id : " + "[" + mId + "]");
				System.out.println("Messages : " + "[" + messages + "]");
				System.out.println("Sent to : " + "[" + receiver + "]");
				System.out.println("Date :" + "[" + date + "]" + "\n");
				System.out.println("\n" + "Type the message id of the message you want to delete");
			}
			Scanner sca = new Scanner(System.in);
			if (sca.hasNextInt()) {
				int mid = sca.nextInt();

				String query2 = ("delete from messages  where mid= ?");
				PreparedStatement stm = connection.prepareStatement(query2);

				stm.setInt(1, mid);

				stm.executeUpdate();
				System.out.println("Message succesfully deleted");
				Write.write("deleted an outbox message", username);
			} else {
				System.out.println("Wrong message id");
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public static void createUser() {
		try {

			System.out.println("Enter username for new user");
			Scanner sca = new Scanner(System.in);
			String usern = sca.next();
			System.out.println("Enter password for new user");
			Scanner scan = new Scanner(System.in);
			String userp = scan.nextLine();
			System.out.println("Enter role for new user");
			Scanner scan2 = new Scanner(System.in);
			String userr = scan.nextLine();
			String query2 = ("insert into users values(null,?,?,?)");
			PreparedStatement stm = connection.prepareStatement(query2);

			stm.setString(1, usern);
			stm.setString(2, userp);
			stm.setString(3, userr);

			stm.executeUpdate();
			Write.write("Admin created a user with username-role ", usern + "-" + userr);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public static void editUserRole() {

		try {
			System.out.println("Type the username of the user you want to edit");

			Scanner sca = new Scanner(System.in);
			String userName = sca.next();
			System.out.println("Type the new role(R ,RE or RED)");
			Scanner scan = new Scanner(System.in);
			String role = scan.nextLine();

			String query2 = ("update users set role= ? where userid= ?");
			PreparedStatement stm = connection.prepareStatement(query2);

			stm.setString(1, role);
			stm.setInt(2, getId(userName));

			stm.executeUpdate();
			System.out.println("User role has changed");
			Write.write("Admin changed " + userName + "'s role to", role);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public static void deleteUser() {

		for (User user : users) {
			System.out.println(user.username);
		}
		System.out.println("Type the username of the user you want to delete");

		Scanner sca = new Scanner(System.in);
		String usernamed = sca.next();
		try {
			String query2 = ("delete from users where users.username= " + "'" + usernamed + "'");
			PreparedStatement stm = connection.prepareStatement(query2);

			stm.executeUpdate();
			Write.write("Admin deleted user with username ", usernamed);
		}

		catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public static boolean exists(String username) {
		boolean x = false;
		for (User user : users) {
			if (user.getUsername().equals(username)) {
				x = true;
				break;
			}
		}

		return x;
	}
}
