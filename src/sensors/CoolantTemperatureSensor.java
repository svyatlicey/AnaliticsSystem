package sensors;

import java.util.Random;

public class CoolantTemperatureSensor implements Sensor {
    private final Random random = new Random();
    private double calibrationOffset = 0;

    @Override
    public double getValue() throws SensorException {
        // 10% chance of sensor failure
        if(random.nextDouble() < 0.1) {
            throw new SensorException("Ошибка датчика температуры охлаждающей жидкости");
        }

        // Generate temperature between 273K (0°C) and 373K (100°C)
        return 273 + random.nextDouble() * 100 + calibrationOffset;
    }

    @Override
    public void calibrate(double offset) {
        this.calibrationOffset = offset;
        System.out.println("Калибровка датчика температуры: смещение " + offset + "K");
    }

    @Override
    public String getType() {
        return "CoolantTempSensor";
    }
}