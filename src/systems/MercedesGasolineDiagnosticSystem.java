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

public class MercedesGasolineDiagnosticSystem extends DiagnosticSystem {

    public MercedesGasolineDiagnosticSystem(SensorKit sensorKit){
        super(sensorKit);

    }
    @Override
    protected void initializeSystem() {
        // 1. Инициализация компонентов Mercedes
        canBus = new CANBus();
        controllers = List.of(
                new EngineMicrocontroller("Mercedes-M256"),
                new PowerController("Mercedes-Power-2024")
        );
        logAnalyzer = new ProxyLogAnalyzer(
                new PredictLogAnalyzer(
                        new BasicLogAnalyzer("MainAnalyzer")
                )
        );
        storage = new SDCard(new UsualStorageImpl());



        // 3. Подключение компонентов
        connectComponents();

        // 4. Установка порогов для Mercedes
        setMercedesThresholds();

        System.out.println("[Mercedes] Бензиновая система готова");
    }

    @Override
    protected String getSystemType() {
        return "Mercedes Gasoline Diagnostic System v1.0";
    }

    private void setMercedesThresholds() {
        // Характерные настройки для двигателя M256
        controllers.get(0).setThresholds("EngineRpm", 900, 7000);
        controllers.get(0).setThresholds("OilPressure", 3.0, 4.8);

        // Настройки системы питания
        controllers.get(1).setThresholds("BatteryVoltage", 12.2, 14.6);
    }

    private void connectComponents() {
        canBus.connect();

        // Стандартная процедура подключения
        for (Microcontroller controller : controllers) {
            controller.connectToBus(canBus);
            for (Sensor sensor : sensorKit.getSensors()) {
                controller.addSensor(sensor);
            }
        }

        logAnalyzer.connectToBus(canBus);
        System.out.println("[Mercedes] Компоненты подключены");
    }
}