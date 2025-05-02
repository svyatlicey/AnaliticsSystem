package sensors.newSensors;

import sensors.Sensor;
import sensors.SensorException;
import sensors.SensorType;
import sensors.SensorTypeFactory;

import java.util.Random;

public class EngineRpmSensor implements Sensor {
    private final Random random = new Random();
    private  double calibrationOffset = 0;
    private final SensorType type;

    public EngineRpmSensor() {
        this.type = SensorTypeFactory.getInstance().getType("EngineRpmSensor");
    }

    @Override
    public double getValue() throws SensorException {
        if(random.nextDouble() < 0.1) { // 10% chance of error
            throw new SensorException("Engine RPM Sensor malfunction!");
        }
        return random.nextInt(7000) + 1000 - calibrationOffset;
    }

    @Override
    public Sensor clone(){
        try {
            EngineRpmSensor cloned = (EngineRpmSensor) super.clone();
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
        return "EngineRpmSensor";
    }
    @Override
    public void add(Sensor sensor){
        throw new UnsupportedOperationException("EngineRpmSensor is a leaf node");
    }

    @Override
    public void remove(Sensor sensor) {
        throw new UnsupportedOperationException("EngineRpmSensor is a leaf node");
    }
    @Override
    public Sensor getSensor(int index) {
        throw new UnsupportedOperationException("EngineRpmSensor is a leaf node");
    }
}