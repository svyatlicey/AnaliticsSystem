package sensors.legosySensors;

public class AnalogTemperatureSensor implements AnalogSensor{
    private static final double MAX_VOLTAGE = 5.0;
    // Возвращает напряжение в вольтах (0-5V)
    public double readVoltage() {
        // Имитация работы датчика
        return Math.random() * MAX_VOLTAGE;
    }
    public AnalogSensor clone(){
        try{
            return (AnalogSensor) super.clone();
        }
        catch(CloneNotSupportedException e){
            throw new RuntimeException("CloneNotSupportedException",e);
        }
    }

}