// Дизельные автомобили
package cars;

import systems.DiagnosticSystem;

public abstract class DieselCar implements Car {
    protected final String model;
    protected final int year;
    protected final String VIN;
    protected final DiagnosticSystem diagnosticSystem;

    public DieselCar(String model, int year, String VIN, DiagnosticSystem diagnosticSystem) {
        this.model = model;
        this.year = year;
        this.VIN = VIN;
        this.diagnosticSystem = diagnosticSystem;
    }
    @Override public String getVIN(){return VIN;}
    @Override public String getModel() { return model; }
    @Override public int getProductionYear() { return year; }
}