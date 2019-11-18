
package entities;

/**
 *
 * @author Patricia
 */
public class DataModel {
    private String identificator;
    private double latitude;
    private double longitude;
    private String timeStampString;

    public DataModel(String identificator, double latitude, double longitude, String timeStampString) {
        this.identificator = identificator;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeStampString = timeStampString;
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

    public String getTimeStampString() {
        return timeStampString;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTimeStampString(String timeStampString) {
        this.timeStampString = timeStampString;
    }
    
    
    
    public String toStringModel(){
    return "id: " +  identificator +
            ", lat: " + latitude + 
            ", lon: " + longitude + 
            ", timestamp: " + timeStampString;
    }
}
