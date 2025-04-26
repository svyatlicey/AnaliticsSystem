package facade;

import analyzer.*;
import communication.CANBus;
import microcontrollers.EngineMicrocontroller;
import microcontrollers.MicrocontrollerGroup;
import microcontrollers.PowerController;
import sensors.Sensor;
import sensors.SensorGroup;
import sensors.legosySensors.AnalogSensorAdapter;
import sensors.legosySensors.AnalogTemperatureSensor;
import sensors.newSensors.BatteryVoltageSensor;
import sensors.newSensors.EngineRpmSensor;
import storage.SDCard;
import storage.UsualStorageImpl;
import storage.StorageDevice;
import storage.StorageException;

import java.util.ArrayList;
import java.util.List;

public class DiagnosticSystemFacade implements SystemFacade{
    private final CANBus canBus;
    private final MicrocontrollerGroup controllers;
    private final LogAnalyzer logAnalyzer;
    private final StorageDevice storage;
    private final List<Sensor> sensors;

    public DiagnosticSystemFacade() {
        this.canBus = new CANBus();
        this.controllers = createControllers();
        this.logAnalyzer = createLogAnalyzerChain();
        this.storage = createStorageDevice();
        initializeSystem(); // Инициализация системы

        sensors = configureSensors(); // Добавление датчиков
        setThresholds(); // Установка пороговых значений
        connectSensorsToControllers(controllers,sensors);
    }

    private MicrocontrollerGroup createControllers() {
        return new MicrocontrollerGroup(
                new EngineMicrocontroller("Engine-01"),
                new PowerController("Power-01")
        );
    }

    private LogAnalyzer createLogAnalyzerChain() {
        return new ProxyLogAnalyzer(
                new PredictLogAnalyzer(
                        new BasicLogAnalyzer("MainAnalyzer")
                )
        );
    }

    private StorageDevice createStorageDevice() {
        return new SDCard(new UsualStorageImpl());
    }

    private void initializeSystem() {
        try {
            // Подключение шины
            canBus.connect();

            // Подключение компонентов к шине
            controllers.connectToBus(canBus);
            logAnalyzer.connectToBus(canBus);

            // Инициализация хранилища
            try {
                storage.initialize();
                System.out.println("SD карта инициализирована");
            } catch (StorageException e) {
                System.err.println("Ошибка SD карты: " + e.getMessage());
                return;
            }

            System.out.println("Система инициализирована");
        } catch (Exception e) {
            System.err.println("Ошибка инициализации: " + e.getMessage());
        }
    }
    private List<Sensor> configureSensors() {
        // Создаем датчики
        Sensor batteryVoltage = new BatteryVoltageSensor();

        Sensor rpm1 = new EngineRpmSensor();
        Sensor rpm2 = new EngineRpmSensor();
        Sensor rpm3 = new EngineRpmSensor();
        Sensor rpm4 = new EngineRpmSensor();
        SensorGroup rpmGroup = new SensorGroup(rpm1, rpm2);
        SensorGroup rpmGroup2 = new SensorGroup(rpm3,rpm4,rpmGroup);

        // Создание и адаптация устаревшего датчика (Адаптер)
        AnalogTemperatureSensor legacyTempSensor = new AnalogTemperatureSensor();
        Sensor adaptedSensor = new AnalogSensorAdapter(
                legacyTempSensor,
                30.0,
                "EngineTemp"
        );

        return new ArrayList<>(List.of(rpmGroup2,adaptedSensor,batteryVoltage));

    }
    private void connectSensorsToControllers(MicrocontrollerGroup controllers, List<Sensor> sensors){
        for (Sensor sensor : sensors) {
            controllers.addSensor(sensor);
        }
    }

    private void setThresholds(){
        // Установка пороговых значений
        controllers.setThresholds("EngineTemp", 80, 110);
        controllers.setThresholds("EngineRpmSensor", 1500, 6500);
        controllers.setThresholds("BatteryVoltageSensor", 11.5, 14.8);
    }
    // Основные методы фасада
    @Override
    public void startDiagnosticSession(int cycles) {
        System.out.println("\n=== Запуск диагностики ===");
        for (int i = 0; i < cycles; i++) {
            System.out.println("\nЦикл диагностики #" + (i+1));
            controllers.processSensorData();
            canBus.processMessages();
            sleep(1000);
        }
    }
    @Override
    public void saveResults(String filename) {
        try {
            boolean success = logAnalyzer.saveReport(storage, filename);
            if (success) {
                System.out.println("\nРезультаты сохранены в: " + filename);
            }
        } catch (Exception e) {
            System.err.println("Ошибка сохранения: " + e.getMessage());
        }
    }
    @Override
    public void printReport() {
        System.out.println("\nИтоговый отчет:");
        System.out.println(logAnalyzer.generateReport());
    }
    @Override
    public void shutdownSystem() {
        controllers.disconnectFromBus();
        canBus.disconnect();
        System.out.println("\nСистема выключена");
    }

    private void sleep(int millis) {
        try { Thread.sleep(millis); }
        catch (InterruptedException ignored) {}
    }
}