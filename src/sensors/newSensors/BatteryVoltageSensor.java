package sensors.newSensors;

import sensors.Sensor;
import sensors.SensorException;

import java.util.Random;

public class BatteryVoltageSensor implements Sensor {
    private final Random random = new Random();
    private double calibrationOffset = 0;

    @Override
    public double getValue() throws SensorException {
        // 15% chance of sensor failure
        if(random.nextDouble() < 0.15) {
            throw new SensorException("Ошибка датчика напряжения АКБ");
        }

        // Generate voltage between 11.5V and 15.0V
        return 11.5 + random.nextDouble() * 3.5 + calibrationOffset;
    }

    @Override
    public void calibrate(double offset) {
        this.calibrationOffset = offset;
        System.out.println("Калибровка датчика напряжения: смещение " + offset + "V");
    }

    @Override
    public String getType() {
        return "BatteryVoltageSensor";
    }
    @Override
    public void add(Sensor sensor){
        throw new UnsupportedOperationException("BatteryVoltageSensor is a leaf node");
    }

    @Override
    public void remove(Sensor sensor) {
        throw new UnsupportedOperationException("BatteryVoltageSensor is a leaf node");
    }
    @Override
    public Sensor getSensor(int index) {
        throw new UnsupportedOperationException("BatteryVoltageSensor is a leaf node");
    }
}