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
public class Vehicle2Test {

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 1000; i++) {
            Thread a = new Thread(new Vehicul());
            a.start();
        }

    }
}
