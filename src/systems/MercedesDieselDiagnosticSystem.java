package systems;

import analyzer.BasicLogAnalyzer;
import analyzer.PredictLogAnalyzer;
import communication.CANBus;
import microcontrollers.EngineMicrocontroller;
import microcontrollers.Microcontroller;
import microcontrollers.PowerController;
import sensors.Sensor;
import sensors.SensorKit;
import sensors.newSensors.*;
import storage.SDCard;
import storage.UsualStorageImpl;
import java.util.List;

public class MercedesDieselDiagnosticSystem extends DiagnosticSystem {

    public MercedesDieselDiagnosticSystem(SensorKit sensorKit) {
        super(sensorKit);
    }
    @Override
    protected void initializeSystem() {
        // 1. Инициализация компонентов Mercedes Diesel
        canBus = new CANBus();
        controllers = List.of(
                new EngineMicrocontroller("Mercedes-OM656"),
                new PowerController("Mercedes-Diesel-Power")
        );
        logAnalyzer = new PredictLogAnalyzer(
                new BasicLogAnalyzer("MainAnalyzer")
        );
        storage = new SDCard(new UsualStorageImpl());


        // 3. Подключение компонентов
        connectComponents();

        // 4. Установка пороговых значений
        setDieselThresholds();

        System.out.println("[Mercedes] Дизельная система инициализирована");
    }

    @Override
    protected String getSystemType() {
        return "Mercedes Diesel Diagnostic System v2.3";
    }

    private void setDieselThresholds() {
        // Характерные настройки для дизеля OM656
        controllers.get(0).setThresholds("EngineRpm", 650, 4200);
        controllers.get(0).setThresholds("OilPressure", 4.0, 6.0);

        controllers.get(1).setThresholds("BatteryVoltage", 11.5, 14.8);
    }

    private void connectComponents() {
        canBus.connect();

        for (Microcontroller controller : controllers) {
            controller.connectToBus(canBus);
            for (Sensor sensor : sensorKit.getSensors()) {
                controller.addSensor(sensor);
            }
        }

        logAnalyzer.connectToBus(canBus);
        System.out.println("[Mercedes] Дизельные компоненты подключены");
    }
}