/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverpk;

import entities.DataModel;

/**
 *
 * @author alexandruborta
 */

public class ModelVehicul {
    private int VehicleID = -1;
    private DataModel data;
    private long lastTime;
    
    public ModelVehicul(int id){
        VehicleID = id;
    }
    
     public ModelVehicul(int id, DataModel dataModel, long time){
        VehicleID = id;
        data = dataModel;
        lastTime = time;
    }

    public int getVehicleID() {
        return VehicleID;
    }

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }

    public long getLastTime() {
        return lastTime;
    }
    
    
    
    
}
