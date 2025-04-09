package sensors;

import java.util.Random;

public class OilPressureSensor implements Sensor {
    private final Random random = new Random();
    private double offset = 0;

    @Override
    public double getValue() throws SensorException {
        if(random.nextDouble() < 0.15) {
            throw new SensorException("Oil pressure sensor fault!");
        }
        return 1.5 + random.nextDouble() * 4.5 + offset;
    }

    @Override
    public void calibrate(double offset) {
        this.offset = offset;
    }

    @Override
    public String getType() {
        return "OilPressureSensor";
    }
}