import analyzer.*;

import communication.*;
import microcontrollers.*;
import sensors.*;
import storage.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // Инициализация компонентов системы
        CANBus canBus = new CANBus();
        SDCard sdCard = new SDCard();

        BasicLogAnalyzer mainAnalyzer = new BasicLogAnalyzer();
        ProxyLogAnalyzer proxyAnalyzer = new ProxyLogAnalyzer(mainAnalyzer, "ProxyAnalyzer"); // Исправленный ID

        EngineMicrocontroller engineController = new EngineMicrocontroller("Engine-01");
        PowerController powerController = new PowerController("Power-01");

        try {
            // Инициализация SD карты
            sdCard.initialize();
            System.out.println("SD карта инициализирована");
        } catch (StorageException e) {
            System.err.println("Ошибка SD карты: " + e.getMessage());
            return;
        }

        // Подключение устройств к шине
        canBus.connect();
        engineController.connectToBus(canBus);
        powerController.connectToBus(canBus);
        proxyAnalyzer.connectToBus(canBus);

        // Добавление всех датчиков
        engineController.addSensor(new CoolantTemperatureSensor());
        engineController.addSensor(new OilPressureSensor());
        engineController.addSensor(new EngineRpmSensor());
        engineController.addSensor(new KnockSensor());

        powerController.addSensor(new GeneratorCurrentSensor());
        powerController.addSensor(new InjectorCurrentSensor());
        powerController.addSensor(new IonCurrentSensor());
        powerController.addSensor(new BatteryVoltageSensor());

        // Установка пороговых значений
        setThresholds(engineController, powerController);

        System.out.println("\n=== Запуск диагностики ===");

        // Имитация работы системы (10 циклов)
        for(int i = 0; i < 5; i++) {
            System.out.println("\nЦикл диагностики #" + (i+1));

            engineController.processSensorData();
            powerController.processSensorData();

            canBus.processMessages();
            Thread.sleep(1000);
        }

        // Сохранение отчета
        try {
            boolean success = mainAnalyzer.saveReport(sdCard, "diagnostics/report.txt");
            if(success) {
                System.out.println("\nОтчет успешно сохранен на SD карту");
            }
        } catch (Exception e) {
            System.err.println("Ошибка сохранения отчета: " + e.getMessage());
        }

        // Отключение устройств
        engineController.disconnectFromBus();
        powerController.disconnectFromBus();
        canBus.disconnect();
    }

    private static void setThresholds(EngineMicrocontroller engine, PowerController power) {
        // Пороги для двигателя
        engine.setThresholds("CoolantTempSensor", 353, 383); // 80-110°C (273+80=353, 273+110=383)
        engine.setThresholds("OilPressureSensor", 2.0, 5.0); // 2.0-5.0 bar
        engine.setThresholds("EngineRpmSensor", 1500, 6500); // RPM
        engine.setThresholds("KnockSensor", 0, 8.0); // Уровень детонации

        // Пороги для системы питания
        power.setThresholds("GeneratorCurrentSensor", 30, 100); // 30-100A
        power.setThresholds("InjectorCurrentSensor", 1.0, 4.5); // 1.0-4.5A
        power.setThresholds("IonCurrentSensor", 1.0, 4.0); // 1.0-4.0mA
        power.setThresholds("BatteryVoltageSensor", 11.5, 14.8); // 11.5-14.8V

        System.out.println("Пороговые значения установлены");
    }
}