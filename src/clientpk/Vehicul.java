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
import java.util.logging.Level;
import java.util.logging.Logger;
import serverpk.MainServer;

/**
 *
 * @author Patricia
 */
public class Vehicul extends Thread {

    static String hostname = "localhost";
    private int vehicleID = -1;
    private BufferedReader fileBuffer;
    private Thread serverThread;
    private boolean alive = true;

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

            //  Thread to take care about server heart beat 
            ServerResponse serverhandler = new ServerResponse(reader, writer);
            serverThread = new Thread(serverhandler);
            serverThread.start();

            while (alive) {
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
            }

            socket.close();

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            //  Stop the thread listening from server when the vehicle is down
            ((ServerResponse) serverThread).Stop();
        }
    }

    private void OpenFileWithId(int id) throws FileNotFoundException, IOException {
        File file = new File("src/data/" + id + ".txt");
        fileBuffer = new BufferedReader(new FileReader(file));
    }

    public int getRandomWithMax(int max) {
        int random = (int) (max * Math.random());
        return random;
    }
    
    public void Stop(){
        alive = false;
    }

    //  Takes care about server response to HeartBeat
    public class ServerResponse extends Thread {

        private BufferedReader reader;
        private PrintWriter writer;
        private boolean alive = true;

        public ServerResponse(BufferedReader reader, PrintWriter writer) {
            this.reader = reader;
            this.writer = writer;
        }

        @Override
        public void run() {
            try {
                while (alive) {
                    //  check if its heartbeat
                    String message = reader.readLine();

                    if (message != null && message.equals("live")) {
                        System.out.println("Vehicle with ID = " + vehicleID + " received HeartBeat. Send live message");
                        writer.println("live");
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(Vehicul.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
        public void Stop(){
            this.alive = false;
        }
    }
}
