package microcontrollers;

import communication.BusDevice;
import communication.BusDeviceDelegate;
import communication.CommunicationInterface;
import sensors.Sensor;
import sensors.SensorException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EngineMicrocontroller implements Microcontroller {
    private final BusDeviceDelegate busDelegate;
    private final Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    private final Map<String, Threshold> thresholds = new ConcurrentHashMap<>();

    public EngineMicrocontroller(String deviceId) {
        this.busDelegate = new BusDeviceDelegate(deviceId, this::handleIncomingMessage);
    }

    // Реализация BusDevice через делегат
    @Override
    public void connectToBus(CommunicationInterface bus) {
        busDelegate.connectToBus(bus);
    }

    @Override
    public void disconnectFromBus() {
        busDelegate.disconnectFromBus();
    }

    @Override
    public String getDeviceId() {
        return busDelegate.getDeviceId();
    }

    @Override
    public void handleMessage(String message) {
        busDelegate.handleMessage(message);
    }

    private void handleIncomingMessage(String message) {
        System.out.printf("[MCU] %s получил сообщение: %s%n", getDeviceId(), message);
        // Логика обработки входящих сообщений
    }

    // Остальная реализация Microcontroller
    @Override
    public void addSensor(Sensor sensor) {
        String type = sensor.getType();
        sensors.put(type, sensor);
        System.out.println("[MCU] " + getDeviceId() + " добавлен датчик: " + type);
    }

    @Override
    public void removeSensor(String sensorType) {
        if (sensors.remove(sensorType) != null) {
            System.out.println("[MCU] " + getDeviceId() + " удален датчик: " + sensorType);
        } else {
            System.out.println("[MCU] " + getDeviceId() + " датчик " + sensorType + " не найден");
        }
    }

    @Override
    public List<Sensor> getConnectedSensors() {
        return new ArrayList<>(sensors.values());
    }

    @Override
    public void processSensorData() {
        sensors.forEach((sensorType, sensor) -> {
            try {
                double value = sensor.getValue();
                checkThresholds(sensor, sensorType, value);
                sendToBus(sensorType, value);
            } catch (SensorException e) {
                handleSensorError(sensorType, e);
            }
        });
    }

    private void checkThresholds(Sensor sensor, String sensorId, double value) {
        Threshold threshold = thresholds.get(sensor.getType());
        if (threshold != null && threshold.isCritical(value)) {
            handleCriticalEvent(sensor, value);
            System.out.printf("[ENG] %s: критическое значение %.2f от датчика %s%n",
                    getDeviceId(), value, sensorId);
        }
    }

    private void sendToBus(String sensorId, double value) {
        if (busDelegate.isConnected()) {
            String message = String.format("{\"engine_sensor\":\"%s\",\"value\":%.2f}", sensorId, value);
            busDelegate.send("MainAnalyzer", message);
        }
    }

    private void handleSensorError(String sensorId, SensorException e) {
        System.err.printf("[MCU] %s ошибка датчика %s: %s%n", getDeviceId(), sensorId, e.getMessage());
        if (busDelegate.isConnected()) {
            String errorMsg = String.format("{\"error\":\"%s\",\"sensor\":\"%s\"}",
                    e.getMessage(), sensorId);
            busDelegate.send("MainAnalyzer", errorMsg);
        }
    }

    @Override
    public void setThresholds(String sensorType, double min, double max) {
        thresholds.put(sensorType, new Threshold(min, max));
        System.out.printf("[MCU] %s установлены пороги для %s: min=%.2f, max=%.2f%n",
                getDeviceId(), sensorType, min, max);
    }

    @Override
    public void handleCriticalEvent(Sensor sensor, double value) {
        String alert = String.format("CRITICAL: %s=%.2f", sensor.getType(), value);
        if (busDelegate.isConnected()) {
            busDelegate.send("MainAnalyzer", alert);
        }
    }
    @Override
    public void add(Microcontroller microcontroller){
        throw new UnsupportedOperationException("EngineMicrocontroller is a leaf node");
    }

    @Override
    public void remove(Microcontroller microcontroller) {
        throw new UnsupportedOperationException("EngineMicrocontroller is a leaf node");
    }
    @Override
    public Microcontroller getMicrocontroller(int index) {
        throw new UnsupportedOperationException("EngineMicrocontroller is a leaf node");
    }
}