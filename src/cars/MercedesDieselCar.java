package cars;

import systems.DiagnosticSystem;


public class MercedesDieselCar extends DieselCar {
    public MercedesDieselCar(String model, int year,String VIN, DiagnosticSystem system) {
        super(model, year,VIN, system);
    }

    @Override
    public void performFullDiagnostic(int cycles) {
        System.out.println("=== Mercedes " + model + " (дизель) ===");
        diagnosticSystem.startDiagnosticSession(cycles);
        diagnosticSystem.saveResults("diagnostics/ " + model + VIN +"report.txt");
    }
}