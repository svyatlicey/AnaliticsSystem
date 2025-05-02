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
    public void startDiagnosticSession(int cycles) {
        System.out.println("\n=== Запуск диагностики ===");
        for (int i = 0; i < cycles; i++) {
            System.out.println("\nЦикл диагностики #" + (i+1));
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
    }

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