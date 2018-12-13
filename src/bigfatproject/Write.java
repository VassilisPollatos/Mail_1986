package bigfatproject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Write {

	public static void write(String event, String username) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime date = LocalDateTime.now();
		String dateFormatted = date.format(formatter);

		try (FileWriter fw = new FileWriter("Log.txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println("Date:\n" + dateFormatted);
			out.println(event + ": " + username);
			out.println("_________________________");

			out.close();
		} catch (IOException e) {

		}

	}

	public static void writeMessage(String sender, String receiver, String text) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime date = LocalDateTime.now();
		String dateFormatted = date.format(formatter);

		try (FileWriter fw = new FileWriter("Log.txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println("Date:\n" + dateFormatted);
			out.println("Message From: " + sender);
			out.println("To: " + receiver);
			out.println("Message: " + text);
			out.println("_________________________");

			out.close();
		} catch (IOException e) {

		}

	}

}
