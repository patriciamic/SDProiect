/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import clientpk.Vehicul;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import serverpk.MainServer;

/**
 *
 * @author alexandruborta
 */
public class ServerTest {

    public static void main(String[] args) throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
        Thread server = new Thread(new MainServer());
        server.start();
    }
}
