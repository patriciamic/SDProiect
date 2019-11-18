package serverpk;

import entities.DataModel;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Patricia
 */
public class MainServer {

    public static final int PORT = 5555;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening on port " + PORT);
            List<DataModel> list = new ArrayList<>();

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    DataModel dataModel = parseLineToDataModel(line);
                    if (dataModel != null) {
                        if (!findDataById(list, dataModel)) {
                            list.add(dataModel);
                        }
                    }
                    writer.println("saved on server");
                }

                System.out.println("Saved: " + list.size() + " items.");
                list.forEach((item) -> {
                    System.out.println(item.toStringModel());
                });
                       
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static DataModel parseLineToDataModel(String line) {
        try {
            String[] parts = line.split(", ");
            String id = parts[0].split("id:")[1];
            double lat = Double.parseDouble(parts[1].split("lat:")[1]);
            double lon = Double.parseDouble(parts[2].split("lon:")[1]);
            String timestamp = parts[3].split("timestamp:")[1];
            return new DataModel(id, lat, lon, timestamp);

        } catch (Exception e) {
            System.out.println(e);
            return null;
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
