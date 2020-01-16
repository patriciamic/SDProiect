package entities;

/**
 *
 * @author Patricia
 */
public class DataModelVehicle {

    private String identificator;
    private double latitude;
    private double longitude;
    private String timeStampString;

    //  Data format : ID, DataStamp , Lat, Long
    //  Example of data : 1,2008-02-04 00:05:13,116.69161,39.85172
    public DataModelVehicle(String line) {
        try {
            String[] parts = line.split(",");

            identificator = parts[0];
            timeStampString = parts[1];
            latitude = Double.parseDouble(parts[2]);
            longitude = Double.parseDouble(parts[3]);

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

    @Override
    public String toString() {
        return identificator
                + "," + timeStampString
                + "," + latitude
                + "," + longitude;
    }
}
