
package clientpk;

import entities.DataModel;
import java.io.BufferedReader;
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
public class MainClient {
    // UDP
    static String hostname = "localhost";

    public static void main(String[] args) {

                try {
                    Socket socket = new Socket(hostname, MainServer.PORT);
                    System.out.println("Client started");

                    InputStream input = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    
                    OutputStream output = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true);

                    //TODO read from file and send data  
                    for (int i = 0; i < 4; i++) {
                        String message = new DataModel(InetAddress.getLocalHost().getHostAddress(), 10.3 + i, 11.2 + i, System.currentTimeMillis() + " ").toStringModel();
                        writer.println(message); 
                        System.out.println("Message sent to server");
//                        System.out.println("Response: " + reader.readLine());
                        Thread.sleep(1000 * getRandomWithMax(5));
                    }
                    
                    System.out.println("I'm out. Bye!");
                    socket.close();
                    
                } catch (Exception ex) {
                    System.out.println(ex);
                } 
    }

    public static int getRandomWithMax(int max) {
        int random = (int)(1 + max * Math.random());
        System.out.println("Delay " + random + " sec");
        return random;
    }
}
