// Бензиновые автомобили
package cars;

import systems.DiagnosticSystem;

public abstract class GasolineCar implements Car {
    protected final String model;
    protected final String VIN;
    protected final int year;
    protected final DiagnosticSystem diagnosticSystem;

    public GasolineCar(String model, int year,String VIN, DiagnosticSystem diagnosticSystem) {
        this.model = model;
        this.VIN = VIN;
        this.year = year;
        this.diagnosticSystem = diagnosticSystem;
    }
    public abstract void doSomethingGasoline();
    @Override public String getVIN(){return VIN;}
    @Override public String getModel() { return model; }
    @Override public int getProductionYear() { return year; }
}