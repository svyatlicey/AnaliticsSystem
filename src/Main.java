import analyzer.*;
import communication.*;
import microcontrollers.*;
import sensors.*;
import sensors.legosySensors.*;
import sensors.newSensors.*;
import storage.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // Инициализация компонентов системы
        CANBus canBus = new CANBus();
        SDCard sdCard = new SDCard();

        // Создание цепочки анализаторов (Декоратор + Прокси)
        BasicLogAnalyzer basicAnalyzer = new BasicLogAnalyzer("MainAnalyzer");
        LogAnalyzer decoratedAnalyzer = new PredictLogAnalyzer(basicAnalyzer);
        ProxyLogAnalyzer proxyAnalyzer = new ProxyLogAnalyzer(decoratedAnalyzer);

        // Создание микроконтроллеров и объединение в группу (Компоновщик)
        EngineMicrocontroller engineController = new EngineMicrocontroller("Engine-01");
        PowerController powerController = new PowerController("Power-01");
        MicrocontrollerGroup controllersGroup = new MicrocontrollerGroup(engineController, powerController);

        try {
            sdCard.initialize();
            System.out.println("SD карта инициализирована");
        } catch (StorageException e) {
            System.err.println("Ошибка SD карты: " + e.getMessage());
            return;
        }

        // Подключение к шине
        canBus.connect();
        controllersGroup.connectToBus(canBus);
        proxyAnalyzer.connectToBus(canBus);

        // 1. Конфигурация датчиков
        // Создание группы датчиков RPM (Компоновщик)
        Sensor rpm1 = new EngineRpmSensor();
        Sensor rpm2 = new EngineRpmSensor();
        Sensor rpm3 = new EngineRpmSensor();
        Sensor rpm4 = new EngineRpmSensor();
        SensorGroup rpmGroup = new SensorGroup(rpm1, rpm2);
        SensorGroup rpmGroup2 = new SensorGroup(rpm3,rpm4,rpmGroup);
        for(Sensor sensor: rpmGroup2) {
            System.out.println(sensor.getType());
        }
        // Создание и адаптация устаревшего датчика (Адаптер)
        AnalogTemperatureSensor legacyTempSensor = new AnalogTemperatureSensor();
        Sensor adaptedSensor = new AnalogSensorAdapter(
                legacyTempSensor,
                30.0,
                "EngineTemp"
        );

        // Подключение датчиков
        // Общие датчики через группу
        controllersGroup.addSensor(rpmGroup2);
        controllersGroup.addSensor(adaptedSensor);

        // Уникальные датчики через конкретные контроллеры
        //EngineController
        controllersGroup.getMicrocontroller(0).addSensor(new CoolantTemperatureSensor());
        controllersGroup.getMicrocontroller(0).addSensor(new OilPressureSensor());
        controllersGroup.getMicrocontroller(0).addSensor(new KnockSensor());
        //PowerController
        controllersGroup.getMicrocontroller(1).addSensor(new GeneratorCurrentSensor());
        controllersGroup.getMicrocontroller(1).addSensor(new InjectorCurrentSensor());
        controllersGroup.getMicrocontroller(1).addSensor(new IonCurrentSensor());
        controllersGroup.getMicrocontroller(1).addSensor(new BatteryVoltageSensor());

        // 2. Установка пороговых значений
        // Общие настройки через группу
        controllersGroup.setThresholds("EngineRpmSensor", 1500, 6500);
        controllersGroup.setThresholds("EngineTemp", 80, 110);

        // Индивидуальные настройки
        engineController.setThresholds("CoolantTempSensor", 353, 383);
        engineController.setThresholds("OilPressureSensor", 2.0, 5.0);
        engineController.setThresholds("KnockSensor", 0, 8.0);

        powerController.setThresholds("GeneratorCurrentSensor", 30, 100);
        powerController.setThresholds("InjectorCurrentSensor", 1.0, 4.5);
        powerController.setThresholds("IonCurrentSensor", 1.0, 4.0);
        powerController.setThresholds("BatteryVoltageSensor", 11.5, 14.8);

        System.out.println("\n=== Запуск диагностики ===");

        // Имитация работы системы
        for(int i = 0; i < 5; i++) {
            System.out.println("\nЦикл диагностики #" + (i+1));

            controllersGroup.processSensorData();
            canBus.processMessages();
            Thread.sleep(1000);
        }

        // Сохранение отчета
        try {
            boolean success = decoratedAnalyzer.saveReport(sdCard, "diagnostics/report.txt");
            if(success) {
                System.out.println("\nОтчет сохранен с прогнозами: " +
                        decoratedAnalyzer.generateReport());
            }
        } catch (Exception e) {
            System.err.println("Ошибка сохранения: " + e.getMessage());
        }

        // Отключение устройств
        controllersGroup.disconnectFromBus();
        canBus.disconnect();
    }
}