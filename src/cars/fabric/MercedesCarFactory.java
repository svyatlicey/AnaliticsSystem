package cars.fabric;

import cars.Car;
import cars.MercedesDieselCar;
import cars.MercedesGasolineCar;
import systems.DiagnosticSystem;
import systems.fabric.DiagnosticSystemFactory;

public class MercedesCarFactory implements CarFactory {
    private DiagnosticSystemFactory diagnosticFactory;
    private int productionCounter = 1;
    @Override
    public void setDiagnosticFactory(DiagnosticSystemFactory factory) {
        this.diagnosticFactory = factory;
    }

    @Override
    public Car createGasolineCar(String model, int year) {
        DiagnosticSystem system = diagnosticFactory.createDiagnosticSystem("gasoline");
        return new MercedesGasolineCar(model, year,generateVIN(year), system);
    }

    @Override
    public Car createDieselCar(String model, int year) {
        DiagnosticSystem system = diagnosticFactory.createDiagnosticSystem("diesel");
        return new MercedesDieselCar(model, year,generateVIN(year), system);
    }
    private String generateVIN(int year){
        return String.format("Mercedes-%d-%06d",year,productionCounter++);
    }
}