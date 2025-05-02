package sensors.newSensors;

import sensors.Sensor;
import sensors.SensorException;
import sensors.SensorType;
import sensors.SensorTypeFactory;

import java.util.Random;

public class OilPressureSensor implements Sensor {
    private final Random random = new Random();
    private double calibrationOffset = 0;
    private final SensorType type;

    public OilPressureSensor() {
        this.type = SensorTypeFactory.getType("OilPressureSensor");
    }


    @Override
    public double getValue() throws SensorException {
        if(random.nextDouble() < 0.15) {
            throw new SensorException("Oil pressure sensor fault!");
        }
        return 1.5 + random.nextDouble() * 4.5 - calibrationOffset;
    }

    @Override
    public Sensor clone(){
        try {
            OilPressureSensor cloned = (OilPressureSensor) super.clone();
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
    public void add(Sensor sensor){
        throw new UnsupportedOperationException("OilPressureSensor is a leaf node");
    }

    @Override
    public void remove(Sensor sensor) {
        throw new UnsupportedOperationException("OilPressureSensor is a leaf node");
    }
    @Override
    public Sensor getSensor(int index) {
        throw new UnsupportedOperationException("OilPressureSensor is a leaf node");
    }
}