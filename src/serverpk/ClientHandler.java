/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverpk;

import entities.DataModelClient;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    DataModelClient dataModelClient;

    public ClientHandler(Socket socket, BufferedReader reader, PrintWriter writer) {
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public void run() {
        System.out.println("ClientHandler start");
        MainServer.WriteLog("ClientHandler start");
        String line;
        try {
            MainServer.WriteLog("CLIENT HANDLER hello client");
            writer.println("hello client");
            while ((line = reader.readLine()) != null) {
                System.out.println("CLIENT HANDLER: Message from client " + line);
                MainServer.WriteLog("CLIENT HANDLER: Message from client " + line);
                if (line.contains("array")) {
                    dataModelClient = new DataModelClient(line.split(":")[1]);
                    MainServer.WriteLog("CLIENT HANDLER: Client model: " + dataModelClient.toString());
                    System.out.println("CLIENT HANDLER: Client model: " + dataModelClient.toString());

                    List<String> list = getClosestVehiclesList(dataModelClient.getLatitude(), dataModelClient.getLongitude());
                    if (!list.isEmpty()) {
                        String responseBuilder = "";
                        for (String string : list) {
                            responseBuilder = responseBuilder + string;
                        }
                        MainServer.WriteLog("CLIENT HANDLER RES: " + responseBuilder);
                        System.out.println("RES: " + responseBuilder);
                        writer.println(responseBuilder);
                    } else {
                        MainServer.WriteLog("CLIENT HANDLER nothing found");
                        writer.println("nothing found");
                    }

                }

                if (line.contains("vehicle selected")) {
                    try {
                        if (MainServer.Vehicles.containsKey(Integer.parseInt(line.split(":")[1]))) {
                            int key = Integer.parseInt(line.split(":")[1]);
                            ModelVehicul model = MainServer.Vehicles.get(key);
                            if (model.getData() != null) {

                                if (model.isBusy()) {
                                    MainServer.WriteLog("CLIENT HANDLER: sorry, this vehicle is busy now.");
                                    writer.println("sorry, this vehicle is busy now.");
                                } else {
                                    model.setBusy(true);
                                    MainServer.Vehicles.put(key, model);
                                    MainServer.WriteLog("CLIENT HANDLER: the vehicle with id: " + key + " has been reserved.");
                                    writer.println("the vehicle with id: " + key + " has been reserved.");
                                }
                            } else {
                                MainServer.WriteLog("CLIENT HANDLER: this vehicle doesn't exist.");
                                writer.println("this vehicle doesn't exist.");
                            }
                        } else {
                            MainServer.WriteLog("CLIENT HANDLER: this vehicle doesn't exist.");
                            writer.println("this vehicle doesn't exist.");
                        }

                    } catch (Exception e) {
                        MainServer.WriteLog("CLIENT HANDLER: something went wrong");
                        writer.println("something went wrong.");
                    }
                }

                Thread.sleep(1000);
            }

        } catch (Exception ex) {
           // Logger.getLogger(VehiculHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    List<String> getClosestVehiclesList(double lat, double lon) {

        Map<Integer, ModelVehicul> vehiclesCopy = new HashMap<>();
        vehiclesCopy.putAll(MainServer.Vehicles);
        List<String> listOfVehicles = new ArrayList<String>();

        MainServer.WriteLog("CLIENT HANDLER: coord from client: " + lat + " , " + lon);
        System.out.println("CLIENT HANDLER: coord from client: " + lat + " , " + lon);
        try {
            vehiclesCopy.entrySet().stream().map((item) -> (ModelVehicul) item.getValue()).forEachOrdered((mv) -> {

                if (!mv.isBusy()) {
                    int distance = getDistance(lat, lon, mv.getData().getLatitude(), mv.getData().getLongitude());
                    if (distance <= 50) {
                        listOfVehicles.add("Vehicle id: " + mv.getVehicleID() + " distance from you is " + distance + " separator");
                    }
                }

            });

        } catch (Exception e) {
            System.out.println("CLIENT HANDLER: ERR: " + e.getMessage());
        }

        return listOfVehicles;
    }

    private int getDistance(double lat, double lon, double latitude, double longitude) {
        double sum = Math.pow((latitude - lat), 2) + Math.pow((longitude - lon), 2);
        return (int) Math.sqrt(sum);
    }

}
