/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author patricia.mic
 */
public class DataModelClient {

    private String identificator;
    private double latitude;
    private double longitude;
    private String formatMessage;
    
    
    //  Data format : ID, Lat, Long
    //  Example of data : 1,116.69161,39.85172
    
    public DataModelClient(String line) {

        try {
            formatMessage = line;
            String[] parts = line.split(",");
            identificator = parts[0];
            latitude = Double.parseDouble(parts[1]);
            longitude = Double.parseDouble(parts[2]);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    

    public String getIdentificator() {
        return identificator;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getFormatMessage() {
        return formatMessage;
    }
    
    @Override
    public String toString(){
    
        return identificator+","+
                latitude+","+
                longitude;
        
    }

}
