package analyzer;

import communication.BusDevice;
import communication.BusDeviceDelegate;
import communication.CommunicationInterface;
import storage.StorageDevice;
import storage.StorageException;

import java.util.ArrayList;
import java.util.List;

public class BasicLogAnalyzer implements LogAnalyzer {
    private final List<String> logs = new ArrayList<>();
    private final BusDeviceDelegate busDeviceDelegate;

    public BasicLogAnalyzer(String deviceId) {
        this.busDeviceDelegate = new BusDeviceDelegate(deviceId,this::analyze);
    }


    @Override
    public void analyze(String rawData) {
        logs.add(rawData);
        System.out.println("[Analyzer] Принято: " + rawData);
    }

    @Override
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== Диагностический отчет ===\n");
        report.append("Всего событий: ").append(logs.size()).append("\n\n");
        logs.forEach(log -> report.append("• ").append(log).append("\n"));
        return report.toString();
    }

    @Override
    public void reset() {
        logs.clear();
        System.out.println("[Analyzer] Логи очищены");
    }

    @Override
    public boolean saveReport(StorageDevice storage, String filename) {
        try {
            storage.write(filename, generateReport());
            System.out.println("[Analyzer] Отчет сохранен в " + filename);
            return true;
        } catch (StorageException e) {
            System.out.println("[Analyzer] Ошибка сохранения: " + e.getMessage());
            return false;
        }
    }

    // Реализация BusDevice
    @Override public String getDeviceId() { return busDeviceDelegate.getDeviceId(); }
    @Override public void connectToBus(CommunicationInterface bus) { busDeviceDelegate.connectToBus(bus); }
    @Override public void disconnectFromBus() { busDeviceDelegate.disconnectFromBus(); }
    @Override public void handleMessage(String message) {} // Не используется напрямую
}