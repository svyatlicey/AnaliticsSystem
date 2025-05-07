package cars;
import cars.DieselCar;
import systems.DiagnosticSystem;

public class BMWDieselCar extends DieselCar {
    public BMWDieselCar(String model, int year,String VIN, DiagnosticSystem system) {
        super(model, year,VIN, system);
    }

    @Override
    public void performFullDiagnostic(int cycles) {
        System.out.println("=== BMW " + model + " (дизель) ===");
        diagnosticSystem.startDiagnosticSession(cycles);
        diagnosticSystem.saveResults("diagnostics/ " + model + VIN +"report.txt");
    }
    @Override
    public void doSomethingDiesel(){
        System.out.println("BMW diesel car is doing something...");
    }
}