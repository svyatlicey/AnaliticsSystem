package systems;

import analyzer.BasicLogAnalyzer;
import analyzer.PredictLogAnalyzer;
import analyzer.ProxyLogAnalyzer;
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

public class BMWGasolineDiagnosticSystem extends DiagnosticSystem {

    public BMWGasolineDiagnosticSystem(SensorKit sensorKit){
        super(sensorKit);
    }
    @Override
    protected void initializeSystem() {
        // 1. Инициализация шины данных
        canBus = new CANBus();

        // 2. Создание микроконтроллеров BMW
        controllers = List.of(
                new EngineMicrocontroller("BMW-N63-Engine"),
                new PowerController("BMW-PowerControl-v2")
        );

        // 3. Настройка цепочки анализаторов логов
        logAnalyzer = new ProxyLogAnalyzer(
                new PredictLogAnalyzer(
                        new BasicLogAnalyzer("MainAnalyzer")
                )
        );

        // 4. Инициализация хранилища
        storage = new SDCard(new UsualStorageImpl());

        // 6. Настройка соединений
        connectComponents();

        // 7. Установка пороговых значений
        setBMWSpecificThresholds();

        System.out.println("[BMW] Gasoline diagnostic system initialized");
    }

    @Override
    protected String getSystemType() {
        return "BMW Gasoline Diagnostic System v2.1";
    }

    private void setBMWSpecificThresholds() {
        // Для двигателя N63
        controllers.get(0).setThresholds("EngineRpm", 850, 6800);
        controllers.get(0).setThresholds("BatteryVoltage", 12.5, 14.7);

        // Для системы питания
        controllers.get(1).setThresholds("FuelPressure", 3.8, 4.2);
        controllers.get(1).setThresholds("CoolantTemperature", 80.0, 120.0);
    }

    public void connectComponents() {
        // 1. Активация шины данных
        canBus.connect();

        // 2. Подключение контроллеров к шине
        for (Microcontroller controller : controllers) {
            controller.connectToBus(canBus);
        }

        // 3. Подключение анализатора логов к шине
        logAnalyzer.connectToBus(canBus);

        // 4. Добавление датчиков в контроллеры
        for (Microcontroller controller : controllers) {
            for (Sensor sensor : sensorKit.getSensors()) {
                controller.addSensor(sensor);
            }
        }

        System.out.println("[BMW] Components connected successfully");
    }
}

