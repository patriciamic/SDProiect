/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import clientpk.Client;

/**
 *
 * @author patricia.mic
 */
public class Client1Test {
    public static void main(String[] args) {
        new Thread(new Client(1)).start();
    }
}
