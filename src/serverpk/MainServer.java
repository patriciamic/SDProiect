package serverpk;

import entities.DataModelVehicle;
import entities.DataModelClient;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricia
 */
public class MainServer implements Runnable {

    static PrintWriter printWriter;
    
    ServerSocket serverSocket;
    List<DataModelVehicle> list;
    public static final int PORT = 5555;
    private int vehicleID = 1;
    //  Vehicles Map with all Vehicles for future use. Need a Vehicle Model, to be able to check if its busy or no
    //  and store insinde a timer that will be used to check the response time. 
    public static Map<Integer, ModelVehicul> Vehicles = new HashMap<>();

    public MainServer() throws FileNotFoundException, UnsupportedEncodingException {
        this.printWriter = new PrintWriter("ServerLog.txt", "UTF-8");
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();

                    InputStream input = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);

                    String message = reader.readLine();

                    if (message.contains("client")) {
                        new Thread(new ClientHandler(socket, reader, writer)).start();
                    } else {
                        //  Send to Vehicle its ID
                        writer.println(vehicleID);

                        System.out.println("New vehicle with ID= " + vehicleID + " connected");
                        //LOG
                        WriteLog("New vehicle with ID= " + vehicleID + " connected");

                        //  Create VehiculHandler where we will handle the receiving strings
                        VehiculHandler handler = new VehiculHandler(vehicleID, socket, reader, writer);
                        new Thread(handler).start();

                        //  Add vehicle to Vehicles Map
                        Vehicles.put(vehicleID, new ModelVehicul(vehicleID));
                        vehicleID++;
                    }

                } catch (IOException ex) {
                    try {
                        socket.close();
                    } catch (IOException ex1) {
                        Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                    Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    static void WriteLog(String s){
        printWriter.println(s);
        printWriter.flush();
    }
}
