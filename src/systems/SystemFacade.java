package systems;

public interface SystemFacade {
    public void startDiagnosticSession();
    public void saveResults(String filename);
    public void printReport();
    public void shutdownSystem();
}
