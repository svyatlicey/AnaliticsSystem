package sensors.newSensors;

import sensors.Sensor;
import sensors.SensorException;
import sensors.SensorType;
import sensors.SensorTypeFactory;

import java.util.Random;

public class GeneratorCurrentSensor implements Sensor {
    private final SensorType type;
    private final Random random = new Random();
    private double calibrationOffset = 0;
    public GeneratorCurrentSensor() {
        this.type = SensorTypeFactory.getType("GeneratorCurrentSensor");
    }

    @Override
    public double getValue() throws SensorException {
        if(random.nextDouble() < 0.15) {
            throw new SensorException("Generator current fault!");
        }
        return random.nextDouble() * 120- calibrationOffset; // 0-120A
    }

    @Override
    public void calibrate(double offset) {
        this.calibrationOffset = offset;
        System.out.println("Калибровка датчика " + getType() + " : смещение " + offset + "K");
    }

    @Override
    public String getType() {
        return type.getName();
    }
    @Override
    public void add(Sensor sensor){
        throw new UnsupportedOperationException("GeneratorCurrentSensor is a leaf node");
    }

    @Override
    public void remove(Sensor sensor) {
        throw new UnsupportedOperationException("GeneratorCurrentSensor is a leaf node");
    }
    @Override
    public Sensor getSensor(int index) {
        throw new UnsupportedOperationException("GeneratorCurrentSensor is a leaf node");
    }
}