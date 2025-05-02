package sensors;

public interface Sensor extends Cloneable {
    double getValue() throws SensorException;
    void calibrate(double offset);
    String getType();
    void add(Sensor sensor);
    void remove(Sensor sensor);
    public Sensor getSensor(int index);
    Sensor clone();
}

