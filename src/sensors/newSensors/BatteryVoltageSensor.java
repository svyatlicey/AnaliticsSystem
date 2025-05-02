package sensors.newSensors;

import sensors.Sensor;
import sensors.SensorException;
import sensors.SensorType;
import sensors.SensorTypeFactory;

import java.util.Random;

public class BatteryVoltageSensor implements Sensor {
    private final SensorType type;
    private final Random random = new Random();
    private double calibrationOffset = 0;

    public BatteryVoltageSensor(){
        this.type = SensorTypeFactory.getType("BatteryVoltageSensor");
    }

    @Override
    public double getValue() throws SensorException {
        // 15% chance of sensor failure
        if(random.nextDouble() < 0.15) {
            throw new SensorException("Ошибка датчика напряжения АКБ");
        }

        // Generate voltage between 11.5V and 15.0V
        return 11.5 + random.nextDouble() * 3.5 - calibrationOffset;
    }
    @Override
    public Sensor clone(){
        try {
            BatteryVoltageSensor cloned = (BatteryVoltageSensor) super.clone();
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