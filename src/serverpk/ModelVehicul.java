/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverpk;

import entities.DataModelVehicle;

/**
 *
 * @author alexandruborta
 */
public class ModelVehicul {

    private int VehicleID = -1;
    private DataModelVehicle data;
    private long lastTime;
    private boolean busy;

    public ModelVehicul(int id) {
        VehicleID = id;
        busy = false;
    }

    public ModelVehicul(int id, DataModelVehicle dataModel, long time) {
        VehicleID = id;
        data = dataModel;
        lastTime = time;
        busy = false;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }
    
    public boolean isBusy(){
        return this.busy;
    }

    public int getVehicleID() {
        return VehicleID;
    }

    public DataModelVehicle getData() {
        return data;
    }

    public void setData(DataModelVehicle data) {
        this.data = data;
    }

    public long getLastTime() {
        return lastTime;
    }

}
