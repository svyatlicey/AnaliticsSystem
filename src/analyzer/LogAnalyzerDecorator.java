package analyzer;

import communication.CommunicationInterface;
import storage.StorageDevice;

public abstract class LogAnalyzerDecorator implements LogAnalyzer{
    LogAnalyzer logAnalyzer;
    public LogAnalyzerDecorator(LogAnalyzer logAnalyzer){
        this.logAnalyzer=logAnalyzer;
    }
    @Override
    public void analyze(String log) {
        logAnalyzer.analyze(log);
    }

    @Override
    public String generateReport() {
        return logAnalyzer.generateReport();
    }

    @Override
    public void reset() {
        logAnalyzer.reset();
    }

    @Override
    public boolean saveReport(StorageDevice storage, String filename) {
        return logAnalyzer.saveReport(storage, filename);
    }

    @Override
    public void handleMessage(String message) {
        logAnalyzer.handleMessage(message);
    }

    @Override
    public String getDeviceId() {
        return logAnalyzer.getDeviceId();
    }

    @Override
    public void connectToBus(CommunicationInterface bus) {
        logAnalyzer.connectToBus(bus);
    }

    @Override
    public void disconnectFromBus() {
        logAnalyzer.disconnectFromBus();
    }
}
