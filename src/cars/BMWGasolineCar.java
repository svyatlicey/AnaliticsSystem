package cars;
import cars.DieselCar;
import cars.GasolineCar;
import systems.DiagnosticSystem;

public class BMWGasolineCar extends GasolineCar {
    public BMWGasolineCar(String model, int year,String VIN, DiagnosticSystem system) {
        super(model, year,VIN, system);
    }

    @Override
    public void performFullDiagnostic() {
        System.out.println("=== BMW " + model + " (бензин) ===");
        diagnosticSystem.startDiagnosticSession();
        diagnosticSystem.saveResults("diagnostics/ " + model + VIN +"report.txt");
    }
    @Override
    public void doSomethingGasoline(){
        System.out.println("do something BMW gasoline");
    }
}

