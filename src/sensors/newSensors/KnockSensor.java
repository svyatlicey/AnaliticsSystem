package sensors.newSensors;

import sensors.Sensor;
import sensors.SensorException;

import java.util.Random;

public class KnockSensor implements Sensor {
    private final Random random = new Random();

    @Override
    public double getValue() throws SensorException {
        if(random.nextDouble() < 0.2) {
            throw new SensorException("Knock sensor malfunction!");
        }
        return random.nextDouble() * 10;
    }

    @Override
    public void calibrate(double offset) {
        System.out.println("Calibrating knock sensor with offset: " + offset);
    }

    @Override
    public String getType() {
        return "KnockSensor";
    }
    @Override
    public void add(Sensor sensor){
        throw new UnsupportedOperationException("KnockSensor is a leaf node");
    }

    @Override
    public void remove(Sensor sensor) {
        throw new UnsupportedOperationException("KnockSensor is a leaf node");
    }
    @Override
    public Sensor getSensor(int index) {
        throw new UnsupportedOperationException("KnockSensor is a leaf node");
    }
}