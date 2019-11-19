/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverpk;

import entities.DataModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexandruborta
 */
public class VehiculHandler implements Runnable {

    final private Socket socket;
    final private BufferedReader reader;
    final private PrintWriter writer;

    public VehiculHandler(Socket socket, BufferedReader reader, PrintWriter writer) {
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public void run() {
        //  Start listening to vehicle
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                DataModel dataModel = new DataModel(line);
                System.out.println(dataModel.toString());
                writer.println("saved on server");
            }
        } catch (IOException ex) {
            Logger.getLogger(VehiculHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
