package sensors.newSensors;

import sensors.Sensor;
import sensors.SensorException;

import java.util.Random;

public class InjectorCurrentSensor implements Sensor {
    private final Random random = new Random();

    @Override
    public double getValue() throws SensorException {
        if(random.nextDouble() < 0.1) {
            throw new SensorException("Injector current error!");
        }
        return 0.5 + random.nextDouble() * 4.5; // 0.5-5.0A
    }

    @Override
    public void calibrate(double offset) {
        System.out.println("Injector current calibration");
    }

    @Override
    public String getType() {
        return "InjectorCurrentSensor";
    }
    @Override
    public void add(Sensor sensor){
        throw new UnsupportedOperationException("InjectorCurrentSensor is a leaf node");
    }

    @Override
    public void remove(Sensor sensor) {
        throw new UnsupportedOperationException("InjectorCurrentSensor is a leaf node");
    }
    @Override
    public Sensor getSensor(int index) {
        throw new UnsupportedOperationException("InjectorCurrentSensor is a leaf node");
    }
}