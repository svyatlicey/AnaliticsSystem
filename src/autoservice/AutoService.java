// AutoService.java
package autoservice;

import autoservice.workers.WorkerPool;
import cars.Car;
import cars.CarIssue;
import cars.car_component.ComponentVisitor;
import owner.Owner;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AutoService {
    private final Queue<Car> carQueue = new ConcurrentLinkedQueue<>();
    private final WorkerPool firstWorkerPool;
    private final ServiceBoxPool boxPool;
    private final ExecutorService executor;
    private final List<WorkerPool> workerPools = null;
    private InvoiceSystem invoiceSystem = null;
    private List<Car> readyCars = null;
    private double balance = 0;

    public AutoService(int numBoxes, WorkerPool... workerPools) {
        this.boxPool = new ServiceBoxPool(numBoxes);
        if(workerPools.length > 0){
            firstWorkerPool = workerPools[0];
            WorkerPool prevWorkerPool = firstWorkerPool;
            for(int i = 1;i < workerPools.length;i++){
                prevWorkerPool.setNextPool(workerPools[i]);
                prevWorkerPool = workerPools[i];
            }
        }else{
            throw new IllegalArgumentException("Не задан ни один рабочий пул");
        }
        this.executor = Executors.newFixedThreadPool(numBoxes);
        invoiceSystem = new InvoiceSystem();
        readyCars = new ArrayList<>();
    }

    public void acceptCar(Car car) {
        car.getCurrentState().enterService(car);
        carQueue.add(car);
        System.out.println("Автомобиль " + car.getVIN() + " принят в очередь");
    }

    public void startProcessing() {
        while (true) {
            Car car = carQueue.poll();
            if (car != null) {
                executor.execute(() -> processCar(car));
            }else{
                break;
            }
            sleepSafe(1000);
        }
    }

    private void processCar(Car car) {
        try (ServiceBox box = boxPool.acquireBox()) {
            System.out.println("Начато обслуживание " + car.getVIN());
            ComponentVisitor componentVisitor = new ComponentVisitor();
            // Проверка компонентов
            componentVisitor.visit(car.getBrakes());
            componentVisitor.visit(car.getTires());
            componentVisitor.visit(car.getClean());

            // Обработка диагностических данных
            componentVisitor.visit(car.getDiagnosticSystem(), car);

            // Добавление новых проблем
            Set<CarIssue> detected = componentVisitor.getDetectedIssues();
            System.out.println("Обнаружено проблем: " + detected);

            for(CarIssue issue : detected) {
                if(!car.getCurrentIssues().contains(issue)) {
                    car.addIssue(issue);
                }
            }

            firstWorkerPool.processCar(car,invoiceSystem);
            // если мы не решили все проблемы
            if(!car.getCurrentIssues().isEmpty()){
                carQueue.add(car); // возвращаем в очередь
            }else{
                readyCars.add(car);
            }
        } catch (Exception e) {
            System.err.println("Ошибка обработки автомобиля: " + e.getMessage());
        }
    }

    private void sleepSafe(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void takeCarsBack(Owner owner){
        List<Car> cars = invoiceSystem.getCarsForOwner(owner);
        for(Car car:cars){
            List<Invoice> invoices = invoiceSystem.getInvoicesForCar(car);
            for(Invoice invoice:invoices){
                pay(owner,invoice);
            }
            if(invoiceSystem.getInvoicesForCar(car).isEmpty());
            readyCars.remove(car);
            car.getCurrentState().exitService(car);
        }
    }
    private void pay(Owner owner,Invoice invoice){
        owner.setBalance(owner.getBalance() - invoice.getAmount());
        invoice.markPaid();
    }
}