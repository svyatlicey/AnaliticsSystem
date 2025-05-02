package sensors.newSensors;

import sensors.Sensor;
import sensors.SensorException;
import sensors.SensorType;
import sensors.SensorTypeFactory;

import java.util.Random;

public class InjectorCurrentSensor implements Sensor {
    private final Random random = new Random();
    private final SensorType type;
    private double calibrationOffset = 0;

    public InjectorCurrentSensor() {
        this.type = SensorTypeFactory.getType("InjectorCurrentSensor");
    }

    @Override
    public double getValue() throws SensorException {
        if(random.nextDouble() < 0.1) {
            throw new SensorException("Injector current error!");
        }
        return 0.5 + random.nextDouble() * 4.5- calibrationOffset; // 0.5-5.0A
    }

    @Override
    public Sensor clone(){
        try {
            InjectorCurrentSensor cloned = (InjectorCurrentSensor) super.clone();
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