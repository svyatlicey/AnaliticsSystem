package sensors;

public interface Sensor {
    double getValue() throws SensorException;
    void calibrate(double offset);
    String getType();
}

