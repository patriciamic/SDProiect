/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import clientpk.Vehicul;
import serverpk.MainServer;

/**
 *
 * @author alexandruborta
 */
public class ServerTest {

    public static void main(String[] args) throws InterruptedException {
        Thread server = new Thread(new MainServer());
        server.start();
    }
}
