package systems.fabric;

import systems.*;
import sensors.SensorKit;
import sensors.newSensors.*;

public class BMWDiagnosticFactory extends DiagnosticSystemFactory {
    private final SensorKit baseKit = createBaseKit();
    private final SensorKit dieselKit = createDieselKit();
    private final SensorKit gasolineKit = createGasolineKit();

    @Override
    public DiagnosticSystem createDiagnosticSystem(String engineType) {


        SensorKit kit = baseKit.clone();

        switch (engineType.toLowerCase()) {
            case "gasoline":
                BMWGasolineDiagnosticSystem gasSystem = new BMWGasolineDiagnosticSystem(kit);
                gasSystem.addSensors(gasolineKit);
                return gasSystem;
            case "diesel":
                BMWDieselDiagnosticSystem diesSystem = new BMWDieselDiagnosticSystem(kit);
                diesSystem.addSensors(dieselKit);
                return diesSystem;
            default:
                throw new IllegalArgumentException("Неизвестный тип двигателя: " + engineType);
        }
    }

    // Базовый набор (общий для всех двигателей)
    private SensorKit createBaseKit() {
        return new SensorKit("BMW-Base")
                .addSensor(new BatteryVoltageSensor())
                .addSensor(new CoolantTemperatureSensor())
                .addSensor(new EngineRpmSensor());
    }

    // Набор для дизельных двигателей
    private SensorKit createDieselKit() {
        return new SensorKit("BMW-Diesel")
                .addSensor(new GeneratorCurrentSensor())
                .addSensor(new OilPressureSensor());
    }

    // Набор для бензиновых двигателей
    private SensorKit createGasolineKit() {
        return new SensorKit("BMW-Gasoline")
                .addSensor(new InjectorCurrentSensor())
                .addSensor(new KnockSensor())
                .addSensor(new IonCurrentSensor());
    }


}