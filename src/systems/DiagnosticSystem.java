package systems;

import analyzer.LogAnalyzer;
import communication.CommunicationInterface;
import microcontrollers.Microcontroller;
import sensors.Sensor;
import sensors.SensorKit;
import storage.StorageDevice;
import java.util.List;

import static java.lang.Thread.sleep;


public abstract class DiagnosticSystem implements SystemFacade {

    protected CommunicationInterface canBus;
    protected  List<Microcontroller> controllers;
    protected LogAnalyzer logAnalyzer;
    protected StorageDevice storage;
    protected SensorKit sensorKit;

    public DiagnosticSystem(SensorKit sensorKit) {
        this.sensorKit = sensorKit;
        initializeSystem();
    }

    public void addSensor(Sensor sensor){
        sensorKit.addSensor(sensor);
        for(Microcontroller controller:controllers){
            controller.addSensor(sensor);
        }
    }
    public void addSensors(SensorKit additionalKit){
        for(Sensor sensor:additionalKit.getSensors()){
            addSensor(sensor.clone());
        }
    }

    // Основной метод инициализации системы (шаблонный метод)
    protected abstract void initializeSystem();

    // Реализация методов интерфейса SystemFacade
    @Override
    public void startDiagnosticSession() {
        System.out.println("\n=== Запуск диагностики ===");
        for(Microcontroller controller:controllers){
            controller.processSensorData();
        }
        canBus.processMessages();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    public StorageDevice getStorage(){return storage;}
    public LogAnalyzer getLogAnalyzer(){return logAnalyzer;}
    @Override
    public void saveResults(String filename) {
        try {
            String report = logAnalyzer.generateReport();
            storage.write(filename, report);
            System.out.println("[System] Report saved to: " + filename);
        } catch (Exception e) {
            System.err.println("[ERROR] Save failed: " + e.getMessage());
        }
    }

    @Override
    public void printReport() {
        System.out.println("\n=== Diagnostic Report ===");
        System.out.println(logAnalyzer.generateReport());
    }

    @Override
    public void shutdownSystem() {
        for(Microcontroller controller:controllers){
            controller.disconnectFromBus();
        }
        logAnalyzer.disconnectFromBus();
        canBus.disconnect();
        System.out.println("[System] " + getSystemType() + " shutdown");
    }
    protected abstract String getSystemType();
}