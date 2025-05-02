import cars.Car;
import cars.fabric.BMWCarFactory;
import cars.fabric.CarFactory;
import cars.fabric.MercedesCarFactory;
import systems.fabric.BMWDiagnosticFactory;
import systems.fabric.DiagnosticSystemFactory;
import systems.fabric.MercedesDiagnosticFactory;

public class Main {
    public static void main(String[] args) {
        // Создаем фабрики систем диагностики
        DiagnosticSystemFactory bmwDiagFactory = new BMWDiagnosticFactory();
        DiagnosticSystemFactory mercedesDiagFactory = new MercedesDiagnosticFactory();

        // Настраиваем автомобильные фабрики
        CarFactory bmwFactory = new BMWCarFactory();
        bmwFactory.setDiagnosticFactory(bmwDiagFactory);

        CarFactory mercedesFactory = new MercedesCarFactory();
        mercedesFactory.setDiagnosticFactory(mercedesDiagFactory);

        // Создаем автомобили
        Car bmwX5 = bmwFactory.createDieselCar("X5", 2025);
        Car mercedesE = mercedesFactory.createDieselCar("E-Class", 2025);

        Car bmwX4 = bmwFactory.createGasolineCar("X4", 2025);
        Car mercedesS = mercedesFactory.createGasolineCar("S-Class", 2025);

        // Запускаем диагностику
        bmwX5.performFullDiagnostic(6);
        mercedesE.performFullDiagnostic(7);
        bmwX4.performFullDiagnostic(6);
        mercedesS.performFullDiagnostic(7);

    }
}