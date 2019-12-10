/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverpk;

import entities.DataModelVehicle;
import entities.DataModelClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author patricia.mic
 */
public class ClientHandler implements Runnable {

    final private Socket socket;
    final private BufferedReader reader;
    final private PrintWriter writer;
    DataModelClient dmc;

    public ClientHandler(Socket socket, BufferedReader reader, PrintWriter writer) {
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public void run() {
        System.out.println("ClientHandler start");
        String line;
        try {
            writer.println("hello new client");
            while ((line = reader.readLine()) != null) {
                //deal with client message
                System.out.println("Message from client " + line);
                if (line.contains("array")) {
                    // array with vehicles
                    writer.println("1, 2, 3, 4");
                }
                
                if(line.contains("vehicle selected")){
                    writer.println("the vehicle with id: " + line.split(":")[1] + " has been reserved.");
                }

                Thread.sleep(1000);
            }

        } catch (Exception ex) {
            Logger.getLogger(VehiculHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
