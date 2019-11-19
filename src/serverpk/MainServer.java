package serverpk;

import clientpk.Vehicul;
import entities.DataModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricia
 */
public class MainServer implements Runnable {

    ServerSocket serverSocket;
    List<DataModel> list;
    public static final int PORT = 5555;
    private int vehicleID = 1;

    //  Vehicles Map with all Vehicles for future use. Need a Vehicle Model, to be able to check if its busy or no
    //  and store insinde a timer that will be used to check the response time. 
    private Map<Integer, ModelVehicul> Vehicles = new HashMap<>();

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

                    //  Send to Vehicle its ID
                    writer.println(vehicleID);
                    System.out.println("New vehicle with ID= +"+vehicleID+" connected");

                    //  Add vehicle to Vehicles Map
                    Vehicles.put(vehicleID, new ModelVehicul(vehicleID));
                    vehicleID++;
                    //  Create VehiculHandler where we will handle the receiving strings
                    new Thread(new VehiculHandler(socket, reader, writer)).start();

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

    private static boolean findDataById(List<DataModel> list, DataModel dataModel) {
        for (DataModel item : list) {
            if (item.getIdentificator().equals(dataModel.getIdentificator())) {
                item.setLatitude(dataModel.getLatitude());
                item.setLongitude(dataModel.getLongitude());
                item.setTimeStampString(dataModel.getTimeStampString());
                return true;
            }
        }
        return false;
    }
}
