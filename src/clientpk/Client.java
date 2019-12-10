/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientpk;

import entities.DataModelClient;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import serverpk.MainServer;

/**
 *
 * @author patricia.mic
 */
public class Client implements Runnable {

    static String hostname = "localhost";
    private int id;

    public Client(int id) {
        this.id = id;
    }

    @Override
    public void run() {

        try {
            System.out.println("Client1 start");

            Socket socket = new Socket(hostname, MainServer.PORT);

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println("client");
            String serverMessage = reader.readLine();
            System.out.println(serverMessage);

            //start meniu
            System.out.println("Do you want to select a vehicle?");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String responseConsole = br.readLine();
//            System.out.println(responseConsole);

            if (getResponse(responseConsole).equals("y")) {

                //send model to server
                DataModelClient clientData = new DataModelClient(id + ",116.69161,39.85172");
                writer.println("request array:" + clientData.getFormatMessage());
                serverMessage = reader.readLine();
                System.out.println(serverMessage);// received array with vehicles

                System.out.println("Select a vehicle id: ");
                responseConsole = br.readLine();

                String res = getResponse(responseConsole);
                if (!res.equals("default") && !res.equals("y") && !res.equals("n")) { //number arrived
                    System.out.println("Your select: " + responseConsole);
                    writer.println("vehicle selected:" + responseConsole);

                    serverMessage = reader.readLine();
                    System.out.println(serverMessage);

                } else {
                    System.out.println("Wrond entry");
                }

            }
            System.out.println("Continue?");
            responseConsole = br.readLine();
            if (getResponse(responseConsole).equals("n")) {
                System.out.println("Do you want to exit?");
                responseConsole = br.readLine();
                if (getResponse(responseConsole).equals("y")) {
                    System.out.println("I'm out");
                }
            }

            if (getResponse(responseConsole).equals("default")) {

            }

        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String getResponse(String responseConsole) {
        switch (responseConsole) {
            case "y":
            case "Y":
            case "yes":
            case "YES":
            case "Yes":
                return "y";
            case "n":
            case "N":
            case "no":
            case "NO":
            case "No":
                return "n";
            default:
                try {
                    Integer.parseInt(responseConsole);
                    return responseConsole;
                } catch (Exception e) {
                    return "default";
                }
        }
    }

}