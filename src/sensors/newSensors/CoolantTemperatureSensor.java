package sensors.newSensors;

import sensors.Sensor;
import sensors.SensorException;
import sensors.SensorType;
import sensors.SensorTypeFactory;

import java.util.Random;

public class CoolantTemperatureSensor implements Sensor {
    private final SensorType type;
    private final Random random = new Random();
    private double calibrationOffset = 0;

    public CoolantTemperatureSensor() {
        this.type = SensorTypeFactory.getType("CoolantTempSensor");
    }
    @Override
    public double getValue() throws SensorException {
        // 10% chance of sensor failure
        if(random.nextDouble() < 0.1) {
            throw new SensorException("Ошибка датчика температуры охлаждающей жидкости");
        }

        // Generate temperature between 273K (0°C) and 373K (100°C)
        return 273 + random.nextDouble() * 100 - calibrationOffset;
    }

    @Override
    public Sensor clone(){
        try {
            CoolantTemperatureSensor cloned = (CoolantTemperatureSensor) super.clone();
            cloned.calibrate(this.calibrationOffset);
            return cloned;
        }catch(CloneNotSupportedException e){
            throw new RuntimeException("Clone failed", e);
        }
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
        throw new UnsupportedOperationException("CoolantTemperatureSensor is a leaf node");
    }

    @Override
    public void remove(Sensor sensor) {
        throw new UnsupportedOperationException("CoolantTemperatureSensor is a leaf node");
    }
    @Override
    public Sensor getSensor(int index) {
        throw new UnsupportedOperationException("CoolantTemperatureSensor is a leaf node");
    }
}