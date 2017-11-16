package loginToRDP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class main {

	public static void main(String[] args) throws IOException {

		ArrayList<String> clientList = new ArrayList<String>();
		Scanner input = new Scanner(System.in);
		String rdpString = "rdp-tcp#";
		String parseClients;

		// Get list of clients
		Process proc = Runtime.getRuntime().exec("qwinsta");
		java.io.InputStream is = proc.getInputStream();

		@SuppressWarnings("resource")
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");

		// Gather the clients as a string
		String consoleOutput = "";
		if (s.hasNext()) {
			consoleOutput = s.next();
		} else {
			consoleOutput = "";
		}

		// Honestly this is just so i don't have to predict how many users there are
		try {

			do {

				parseClients = consoleOutput.substring(consoleOutput.indexOf(rdpString) + rdpString.length() + 2);

				// delete all info after the id number
				consoleOutput = parseClients.substring(parseClients.indexOf("Active"));
				parseClients = parseClients.substring(0, parseClients.indexOf("Active"));

				clientList.add(parseClients.replaceAll("^\\s+", "")); // get rid of all spaces before the client name

			} while (consoleOutput.substring(consoleOutput.indexOf(rdpString)) != null);

		} catch (Exception e) {

		} finally {

			System.out.println("Let's Get You Connected to A Remote Client!\n");
			System.out.println("Client Name \t      ID Number");
			System.out.println("----------- \t      ---------");

			// Display the list of clients
			for (int i = 0; i < clientList.size(); i++) {
				System.out.println(clientList.get(i));
			}

			System.out.println("\nNow type the ID of the client you want to connect to: ");
			proc = Runtime.getRuntime().exec("mstsc.exe " + input.next() + " /shadow"); // Connect to the session
			System.out.println("Enjoy!");

		}
	}
}
