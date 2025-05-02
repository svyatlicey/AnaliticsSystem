package cars;

public interface Car {
    void performFullDiagnostic(int cycles);
    String getModel();
    int getProductionYear();
    String getVIN();

}