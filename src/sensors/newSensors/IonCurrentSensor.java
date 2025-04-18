package sensors.newSensors;

import sensors.Sensor;
import sensors.SensorException;

import java.util.Random;

public class IonCurrentSensor implements Sensor {
    private final Random random = new Random();
    private double calibrationOffset = 0;

    @Override
    public double getValue() throws SensorException {
        // 12% вероятность ошибки
        if(random.nextDouble() < 0.12) {
            throw new SensorException("Ion current sensor fault detected!");
        }

        // Генерация значений в диапазоне 0.5-4.5 мА с калибровкой
        return (0.5 + random.nextDouble() * 4.0) + calibrationOffset;
    }

    @Override
    public void calibrate(double offset) {
        this.calibrationOffset = offset;
        System.out.println("Ion current sensor calibrated with offset: " + offset + " mA");
    }

    @Override
    public String getType() {
        return "IonCurrentSensor";
    }
    public void add(Sensor sensor){
        throw new UnsupportedOperationException("IonCurrentSensor is a leaf node");
    }

    @Override
    public void remove(Sensor sensor) {
        throw new UnsupportedOperationException("IonCurrentSensor is a leaf node");
    }
    @Override
    public Sensor getSensor(int index) {
        throw new UnsupportedOperationException("IonCurrentSensor is a leaf node");
    }
}