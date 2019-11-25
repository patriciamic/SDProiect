package serverpk;

import entities.DataModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricia
 */
public class MainServer implements Runnable, VehiculHandler.DataModelChange {

    ServerSocket serverSocket;
    List<DataModel> list;
    public static final int PORT = 5555;
    private int vehicleID = 1;
    //  Vehicles Map with all Vehicles for future use. Need a Vehicle Model, to be able to check if its busy or no
    //  and store insinde a timer that will be used to check the response time. 
    private Map<Integer, ModelVehicul> Vehicles = new HashMap<>();

    private Timer timer;
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Map<Integer, ModelVehicul> vehiclesCopy = new HashMap<>();
            vehiclesCopy.putAll(Vehicles);
            System.out.println("----------------------------------------");
            System.out.println("Vehicle copy");

            vehiclesCopy.entrySet().stream().map((item) -> (ModelVehicul) item.getValue()).forEachOrdered((mv) -> {
                System.out.println(mv.getData().toString());

                long time = System.currentTimeMillis() - mv.getLastTime();
                int seconds = (int) (time / 1000);
                seconds = seconds % 60;
                System.out.println("Seconds: " + seconds);

                // I set 5 here for test, but should be 10
                if (seconds > 5) {
                    //TODO Check to see if it is alive 
                    System.out.println("Check to see if it it alive for VehicleId: " + mv.getVehicleID());
                }

            });

            System.out.println("----------------------------------------");
        }
    };

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening on port " + PORT);

            timer = new Timer();
            timer.scheduleAtFixedRate(timerTask, 0, 5000);

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
                    System.out.println("New vehicle with ID= +" + vehicleID + " connected");

                    //  Create VehiculHandler where we will handle the receiving strings
                    VehiculHandler handler = new VehiculHandler(socket, reader, writer);
                    handler.setListener(this);
                    new Thread(handler).start();

                    //  Add vehicle to Vehicles Map
                    Vehicles.put(vehicleID, new ModelVehicul(vehicleID));
                    vehicleID++;

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

    @Override
    public void onDataModelChanged(DataModel data, long lastTime) {
        // Update ModelVehicul with whole data
        Vehicles.put(Integer.parseInt(data.getIdentificator()),
                new ModelVehicul(Integer.parseInt(data.getIdentificator()), data, lastTime));

    }

}
