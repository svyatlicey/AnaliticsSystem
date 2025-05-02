package cars.fabric;


import cars.*;
import systems.fabric.DiagnosticSystemFactory;
import systems.DiagnosticSystem;

public class BMWCarFactory implements CarFactory {
    private DiagnosticSystemFactory diagnosticFactory;
    private int productionCounter = 1;
    @Override
    public void setDiagnosticFactory(DiagnosticSystemFactory factory) {
        this.diagnosticFactory = factory;
    }

    @Override
    public Car createGasolineCar(String model, int year) {
        DiagnosticSystem system = diagnosticFactory.createDiagnosticSystem("gasoline");
        return new BMWGasolineCar(model, year,generateVIN(year), system);
    }

    @Override
    public Car createDieselCar(String model, int year) {
        DiagnosticSystem system = diagnosticFactory.createDiagnosticSystem("diesel");
        return new BMWDieselCar(model, year,generateVIN(year), system);
    }
    private String generateVIN(int year){
        return String.format("BMW-%d-%06d",year,productionCounter++);
    }
}

