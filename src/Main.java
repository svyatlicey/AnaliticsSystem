import autoservice.AutoService;
import autoservice.workers.*;
import cars.Car;
import cars.fabric.BMWCarFactory;
import cars.fabric.CarFactory;
import cars.fabric.MercedesCarFactory;
import cars.states.InServiceState;
import owner.CarOwner;
import owner.Owner;
import systems.fabric.BMWDiagnosticFactory;
import systems.fabric.DiagnosticSystemFactory;
import systems.fabric.MercedesDiagnosticFactory;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // 1. Инициализация фабрик
        DiagnosticSystemFactory bmwDiagFactory = new BMWDiagnosticFactory();
        DiagnosticSystemFactory mercedesDiagFactory = new MercedesDiagnosticFactory();

        CarFactory bmwFactory = new BMWCarFactory();
        bmwFactory.setDiagnosticFactory(bmwDiagFactory);

        CarFactory mercedesFactory = new MercedesCarFactory();
        mercedesFactory.setDiagnosticFactory(mercedesDiagFactory);

        // 2. Создание владельцев
        Owner john = new CarOwner("John Doe");
        Owner emma = new CarOwner("Emma Smith");

        // 3. Создание и привязка автомобилей
        Car bmwX5 = bmwFactory.createDieselCar("X5", 2023);
        bmwX5.setOwner(john);

        Car mercedesE = mercedesFactory.createDieselCar("E-Class", 2023);
        mercedesE.setOwner(emma);

        Car bmwX4 = bmwFactory.createGasolineCar("X4", 2023);
        bmwX4.setOwner(john);

        Car mercedesS = mercedesFactory.createGasolineCar("S-Class", 2023);
        mercedesS.setOwner(emma);

        // 4. Первая серия поездок
        System.out.println("\n=== Первая серия поездок ===");
        rideAndShowStatus(bmwX5, 10);
        rideAndShowStatus(mercedesE, 15);
        rideAndShowStatus(bmwX4, 8);
        rideAndShowStatus(mercedesS, 12);

        // 5. Создание и настройка автосервиса
        WorkerPool brakePool = new WorkerPool(2, BrakeMaster.class);
        WorkerPool tirePool = new WorkerPool(3, TireMaster.class);
        WorkerPool cleanPool = new WorkerPool(1, CleaningMaster.class);
        WorkerPool diagnosticPool = new WorkerPool(2, DiagnosticMaster.class);

        brakePool.setNextPool(tirePool);
        tirePool.setNextPool(cleanPool);
        cleanPool.setNextPool(diagnosticPool);

        AutoService service = new AutoService(4, brakePool, tirePool, cleanPool, diagnosticPool);

        // 6. Прием автомобилей на обслуживание
        System.out.println("\n=== Автомобили поступают в сервис ===");
        service.acceptCar(bmwX5);
        service.acceptCar(mercedesE);
        service.acceptCar(bmwX4);
        service.acceptCar(mercedesS);

        // 7. Процесс ремонта
        System.out.println("\n=== Начало работ в сервисе ===");
        service.startProcessing();

        // 8. Ожидание завершения работ
        Thread.sleep(5000);

        // 9. Возврат автомобилей владельцам
        System.out.println("\n=== Владельцы забирают автомобили ===");
        service.takeCarsBack(john);
        service.takeCarsBack(emma);

        // 10. Вывод информации о владельцах
        printOwnerInfo(john);
        printOwnerInfo(emma);

        // 11. Вторая серия поездок после ремонта
        System.out.println("\n=== Поездки после ремонта ===");
        rideAndShowStatus(bmwX5, 5);
        rideAndShowStatus(mercedesE, 7);
        rideAndShowStatus(bmwX4, 4);
        rideAndShowStatus(mercedesS, 6);

        // 12. Финальный статус
        System.out.println("\n=== Финальное состояние автомобилей ===");
        printCarStatus("BMW X5", bmwX5);
        printCarStatus("Mercedes E-Class", mercedesE);
        printCarStatus("BMW X4", bmwX4);
        printCarStatus("Mercedes S-Class", mercedesS);
    }

    private static void rideAndShowStatus(Car car, int seconds) {
        System.out.println("\n" + car.getOwner().getName() + " начинает поездку на " + car.getModel());
        car.ride(seconds);
        printCarStatus(car.getModel(), car);
    }

    private static void printCarStatus(String name, Car car) {
        String status = car.getCurrentState() instanceof InServiceState ?
                "В сервисе" : "На дороге";
        String issues = car.getCurrentIssues().isEmpty() ?
                "Нет проблем" : car.getCurrentIssues().toString();

        System.out.printf("%-20s VIN: %-15s Статус: %-10s Проблемы: %-30s%n",
                name,
                car.getVIN().substring(0, 8) + "...",
                status,
                issues);
    }

    private static void printOwnerInfo(Owner owner) {
        System.out.printf("%-20s Баланс: %-10.2f руб.",
                owner.getName(),
                owner.getBalance());
    }
}