package sensors.legosySensors;

import sensors.Sensor;
import sensors.legosySensors.AnalogTemperatureSensor;
import sensors.SensorException;

public class AnalogSensorAdapter implements Sensor {
    private final AnalogSensor legacySensor;
    private final double scaleFactor;
    private final String type;
    private double calibrationOffset = 0.0;

    public AnalogSensorAdapter(AnalogTemperatureSensor legacySensor,
                               double scaleFactor,
                               String type) {
        this.legacySensor = legacySensor;
        this.scaleFactor = scaleFactor;
        this.type = type;
    }

    @Override
    public double getValue() throws SensorException {
        try {
            double voltage = legacySensor.readVoltage();
            return (voltage * scaleFactor) + calibrationOffset;
        } catch (Exception e) {
            throw new SensorException("Ошибка аналогового датчика " + type);
        }
    }

    @Override
    public void calibrate(double offset) {
        this.calibrationOffset = offset;
    }

    @Override
    public String getType() {
        return type;
    }

    // Методы композиции (для совместимости с SensorGroup)
    @Override
    public void add(Sensor sensor) {
        throw new UnsupportedOperationException("Адаптер является терминальным компонентом");
    }

    @Override
    public void remove(Sensor sensor) {
        throw new UnsupportedOperationException("Адаптер является терминальным компонентом");
    }

    @Override
    public Sensor getSensor(int index) {
        throw new UnsupportedOperationException("Адаптер не содержит вложенных компонентов");
    }
}