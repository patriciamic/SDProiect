package clientpk;

import entities.DataModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import serverpk.MainServer;

/**
 *
 * @author Patricia
 */
public class Vehicul implements Runnable {

    // UDP
    static String hostname = "localhost";
    private int vehicleID = -1;
    private BufferedReader fileBuffer;

    @Override
    public void run() {
        try {
            Socket socket = new Socket(hostname, MainServer.PORT);

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            //  Get my ID
            vehicleID = Integer.parseInt(reader.readLine());
            //  Open the file with data. The file name is "vehicleID.txt"
            OpenFileWithId(vehicleID);

            //  Start sending messages to server
            try {
                String line;
                while ((line = fileBuffer.readLine()) != null) {
                    writer.println(line);
                    Thread.sleep(1000 * getRandomWithMax(10));
                }
            } finally {
                fileBuffer.close();
            }

            socket.close();

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private void OpenFileWithId(int id) throws FileNotFoundException, IOException {
        File file = new File("src/data/" + id + ".txt");
        fileBuffer = new BufferedReader(new FileReader(file));
    }

    public static int getRandomWithMax(int max) {
        int random = (int) (3 + max * Math.random());
        return random;
    }
}
