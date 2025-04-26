package sensors.newSensors;

import sensors.Sensor;
import sensors.SensorException;
import sensors.SensorType;
import sensors.SensorTypeFactory;

import java.util.Random;

public class IonCurrentSensor implements Sensor {
    private final Random random = new Random();
    private double calibrationOffset = 0;
    private final SensorType type;

    public IonCurrentSensor() {
        this.type = SensorTypeFactory.getType("IonCurrentSensor");
    }

    @Override
    public double getValue() throws SensorException {
        // 12% вероятность ошибки
        if(random.nextDouble() < 0.12) {
            throw new SensorException("Ion current sensor fault detected!");
        }

        // Генерация значений в диапазоне 0.5-4.5 мА с калибровкой
        return (0.5 + random.nextDouble() * 4.0) - calibrationOffset;
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