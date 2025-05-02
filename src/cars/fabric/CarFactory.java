package cars.fabric;

import cars.Car;
import systems.fabric.DiagnosticSystemFactory;

public interface CarFactory {
    Car createGasolineCar(String model, int year);
    Car createDieselCar(String model, int year);

    // Новая зависимость - фабрика систем диагностики
    void setDiagnosticFactory(DiagnosticSystemFactory factory);
}