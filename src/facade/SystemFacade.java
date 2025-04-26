package facade;

public interface SystemFacade {
    public void startDiagnosticSession(int cycles);
    public void saveResults(String filename);
    public void printReport();
    public void shutdownSystem();
}
