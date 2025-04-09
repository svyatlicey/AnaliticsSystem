package analyzer;

import communication.BusDevice;
import communication.CommunicationInterface;
import java.util.Random;
import storage.StorageDevice;
public class ProxyLogAnalyzer implements LogAnalyzer {
    private final LogAnalyzer targetAnalyzer;
    private final String deviceId;
    private final Random random = new Random();
    private CommunicationInterface bus;

    public ProxyLogAnalyzer(LogAnalyzer target, String id) {
        this.targetAnalyzer = target;
        this.deviceId = id;
    }

    @Override
    public void handleMessage(String message) {
        // 10% шанс искажения сообщения
        String modifiedMessage = message;
        if(random.nextDouble() < 0.1) {
            modifiedMessage = corruptMessage(message);
            System.out.println("[Proxy] Искажено сообщение: " + message + " -> " + modifiedMessage);
        }

        targetAnalyzer.analyze(modifiedMessage);
    }

    private String corruptMessage(String original) {
        if(original.length() < 5) return original;
        int position = random.nextInt(original.length() - 2) + 1;
        return original.substring(0, position)
                + (char)(original.charAt(position) + 1)
                + original.substring(position + 1);
    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public void connectToBus(CommunicationInterface bus) {
        this.bus = bus;
        bus.registerDevice(this);
    }

    @Override
    public void disconnectFromBus() {
        if(bus != null) {
            bus.unregisterDevice(deviceId);
            bus = null;
        }
    }

    // Делегированные методы
    @Override public void analyze(String rawData) { targetAnalyzer.analyze(rawData); }
    @Override public String generateReport() { return targetAnalyzer.generateReport(); }
    @Override public void reset() { targetAnalyzer.reset(); }
    @Override public boolean saveReport(StorageDevice storage, String filename) {
        return targetAnalyzer.saveReport(storage, filename);
    }
}