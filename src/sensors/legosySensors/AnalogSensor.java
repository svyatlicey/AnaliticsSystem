package sensors.legosySensors;

public interface AnalogSensor extends Cloneable {
    double readVoltage();
    AnalogSensor clone();
}
