package cars;


import systems.DiagnosticSystem;

public class MercedesGasolineCar extends GasolineCar {
    public MercedesGasolineCar(String model, int year,String VIN, DiagnosticSystem system) {
        super(model, year,VIN, system);
    }

    @Override
    public void performFullDiagnostic() {
        System.out.println("=== Mercedes " + model + " (бензин) ===");
        diagnosticSystem.startDiagnosticSession();
        diagnosticSystem.saveResults("diagnostics/ " + model + VIN +"report.txt");
    }
    @Override
    public void doSomethingGasoline(){
        System.out.println("do something gasoline Mercedes");
    }
}