package sensors;

import java.util.Random;

public class EngineRpmSensor implements Sensor {
    private final Random random = new Random();

    @Override
    public double getValue() throws SensorException {
        if(random.nextDouble() < 0.1) { // 10% chance of error
            throw new SensorException("Engine RPM Sensor malfunction!");
        }
        return random.nextInt(7000) + 1000;
    }

    @Override
    public void calibrate(double offset) {
        System.out.println("Calibrating Engine RPM Sensor with offset: " + offset);
    }

    @Override
    public String getType() {
        return "EngineRpmSensor";
    }
}