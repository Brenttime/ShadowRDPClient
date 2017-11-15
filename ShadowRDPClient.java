/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shadowrdpclient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Brenttime
 */
public class ShadowRDPClient {
    
    //Giving the gui access to the lists
    public static ArrayList<String> clientList = new ArrayList<String>();     
    public static ArrayList<String> clientID = new ArrayList<String>();
    public static rdpGui gui = new rdpGui();
    public static Process proc;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        
        String rdpString = "rdp-tcp#";
        String parseClients;
      
        gui.setVisible(true);
        gui.setResizable(false);
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
                        parseClients = parseClients.substring(0, parseClients.indexOf("Active")).replaceAll("^\\s+", "");
                        clientID.add(parseClients.substring((parseClients.toCharArray().length) - 3));
                        
                        
                        //clientList.add(parseClients); // get rid of all spaces before the client name

                        clientList.add(parseClients.substring(0, 
                                parseClients.indexOf(clientID.get(clientID.size() - 1)))
                                .replaceAll("^\\s+", "")); // get rid of all spaces before the client name
                        
                } while (consoleOutput.substring(consoleOutput.indexOf(rdpString)) != null);

        } catch (Exception e) {

        } finally {

            Scanner input = new Scanner(System.in);
            System.out.println("Let's Get You Connected to A Remote Client!\n");
            System.out.println("Client Name \t      ID Number");
            System.out.println("----------- \t      ---------");
            
            // Display the list of clients
            for (int i = 0; i < clientList.size(); i++) {
                  gui.ddlClientList.addItem(clientList.get(i));
                  System.out.println(clientList.get(i));
            }
            System.out.println("\nNow type the ID of the client you want to connect to: ");
            proc = Runtime.getRuntime().exec("mstsc.exe " + input.next() + " /shadow"); // Connect to the session
            System.out.println("Enjoy!");
        }
    }
    public static void startConnection() throws IOException
    {
        proc = Runtime.getRuntime().exec("mstsc.exe " +  clientID.get(gui.ddlClientList.getSelectedIndex()) + " /shadow"); // Connect to the session
    }
}
