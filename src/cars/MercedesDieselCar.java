package cars;

import systems.DiagnosticSystem;


public class MercedesDieselCar extends DieselCar {
    public MercedesDieselCar(String model, int year,String VIN, DiagnosticSystem system) {
        super(model, year,VIN, system);
    }

    @Override
    public void performFullDiagnostic() {
        System.out.println("=== Mercedes " + model + " (дизель) ===");
        diagnosticSystem.startDiagnosticSession();
        diagnosticSystem.saveResults("diagnostics/ " + model + VIN +"report.txt");
    }
    @Override
    public void doSomethingDiesel(){
        System.out.println("do something diesel Mercedes");
    }
}