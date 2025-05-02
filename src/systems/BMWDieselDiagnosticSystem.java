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

public class BMWDieselDiagnosticSystem extends DiagnosticSystem {

    public BMWDieselDiagnosticSystem(SensorKit sensorKit) {
        super(sensorKit);
    }

    @Override
    protected void initializeSystem() {
        // 1. Инициализация компонентов
        canBus = new CANBus();
        controllers = List.of(
                new EngineMicrocontroller("BMW-B57-Diesel"),
                new PowerController("BMW-Diesel-Power")
        );
        logAnalyzer = new PredictLogAnalyzer(
                new BasicLogAnalyzer("MainAnalyzer")
        );
        storage = new SDCard(new UsualStorageImpl());


        // 3. Подключение компонентов
        connectComponents();

        // 4. Установка пороговых значений
        setDieselThresholds();

        System.out.println("[BMW] Дизельная система инициализирована");
    }

    @Override
    protected String getSystemType() {
        return "BMW Diesel Diagnostic System v3.0";
    }

    private void setDieselThresholds() {
        // Для дизельного двигателя
        controllers.get(0).setThresholds("EngineRpm", 700, 4000);
        controllers.get(0).setThresholds("OilPressure", 3.5, 5.5);

        // Для системы питания
        controllers.get(1).setThresholds("BatteryVoltage", 11.8, 14.5);
    }

    private void connectComponents() {
        // Аналогично бензиновой версии
        canBus.connect();

        for (Microcontroller controller : controllers) {
            controller.connectToBus(canBus);
            for (Sensor sensor : sensorKit.getSensors()) {
                controller.addSensor(sensor);
            }
        }

        logAnalyzer.connectToBus(canBus);
        System.out.println("[BMW] Компоненты подключены");
    }
}