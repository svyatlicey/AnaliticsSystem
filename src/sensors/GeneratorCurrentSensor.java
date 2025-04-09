package sensors;

import java.util.Random;

public class GeneratorCurrentSensor implements Sensor {
    private final Random random = new Random();

    @Override
    public double getValue() throws SensorException {
        if(random.nextDouble() < 0.15) {
            throw new SensorException("Generator current fault!");
        }
        return random.nextDouble() * 120; // 0-120A
    }

    @Override
    public void calibrate(double offset) {
        System.out.println("Generator current calibration");
    }

    @Override
    public String getType() {
        return "GeneratorCurrentSensor";
    }
}