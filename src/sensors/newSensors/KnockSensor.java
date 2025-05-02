package sensors.newSensors;

import sensors.Sensor;
import sensors.SensorException;
import sensors.SensorType;
import sensors.SensorTypeFactory;

import java.util.Random;

public class KnockSensor implements Sensor {
    private final Random random = new Random();
    private  double calibrationOffset = 0;
    private final SensorType type;

    public KnockSensor() {
        this.type = SensorTypeFactory.getInstance().getType("KnockSensor");
    }

    @Override
    public double getValue() throws SensorException {
        if(random.nextDouble() < 0.2) {
            throw new SensorException("Knock sensor malfunction!");
        }
        return random.nextDouble() * 10- calibrationOffset;
    }

    @Override
    public Sensor clone(){
        try {
            KnockSensor cloned = (KnockSensor) super.clone();
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