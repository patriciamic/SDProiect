/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverpk;

import entities.DataModelVehicle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
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
    DataModelVehicle dataModel;
    private Timer internalTimer;
    private Timer heartBeatTimer;
    private int vehicleID = 1;
    private int seconds = 0;
    private int heartSeconds = 0;
    private boolean shutdown = false;

    public VehiculHandler(int id, Socket socket, BufferedReader reader, PrintWriter writer) {
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
        this.vehicleID = id;
        //  Config and start the timer
        StartInternalTimer();
    }

    @Override
    public void run() {
        while (!shutdown) {
            //  Start listening to vehicle
            String line;
            try {

                while ((line = reader.readLine()) != null) {
                    //  Each time receive coord from vehicle, reset timer to 0
                    resetInternalTimer();
                    //  Check if  its heartbeat response or no
                    if (!line.equals("live")) {
                        dataModel = new DataModelVehicle(line);
                        System.out.println(line);
                    } else {

                        //  if it is heartbeat response -> stop heartbeat timer, and restart the internal timer
                        System.out.println("Server received live message from Vehicle with ID = " + vehicleID);
                        resetHeartBeatTimer();
                        stopHeartBeatTimer();
                        resetInternalTimer();
                        StartInternalTimer();
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(VehiculHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //  Config and starts the timer.
    //  Once in a second makes a check. It will reach 5 seconds only if no
    //  coords are received in 5 sec
    private void StartInternalTimer() {
        internalTimer = new Timer();
        internalTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                seconds++;

                if (seconds >= 5) {
                    SendHeartBeat();
                    // Stop the timer
                    internalTimer.cancel();
                }
            }
        }, 1000, 1000);
    }

    //  HeartbeatTimer. Time wait for response is 3 seconds
    private void StartHeartBeatTimer() {
        heartBeatTimer = new Timer();
        heartBeatTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                heartSeconds++;

                if (heartSeconds >= 3) {
                    KillVehicle();
                    // Stop the timer
                    heartBeatTimer.cancel();
                }
            }
        }, 1000, 1000);
    }

    private void resetInternalTimer() {
        this.seconds = 0;
    }

    private void resetHeartBeatTimer() {
        this.heartSeconds = 0;
    }

    private void stopHeartBeatTimer() {
        if (heartBeatTimer != null) {
            heartBeatTimer.cancel();
        }
    }

    private void stopInternalTimer() {
        internalTimer.cancel();
    }

    public DataModelVehicle getDataModel() {
        return dataModel;
    }

    private void SendHeartBeat() {
        // Call HeartBeat
        System.out.println("Sent HeartBeat for vehicle with ID = " + vehicleID);
        writer.println("live");
        stopInternalTimer();
        resetInternalTimer();
        resetHeartBeatTimer();
        StartHeartBeatTimer();
    }

    private void KillVehicle() {
        System.out.println("Vehicle with ID = " + vehicleID + " is dead, Removing it from pool");

        resetHeartBeatTimer();
        resetInternalTimer();
        if (heartBeatTimer != null) {
            stopHeartBeatTimer();
        }
        if (internalTimer != null) {
            stopInternalTimer();
        }

        shutdown = true;
    }

}
