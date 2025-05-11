
package autoservice.workers;

import autoservice.InvoiceSystem;
import autoservice.ServiceReport;
import cars.Car;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorkerPool {
    private final Queue<Worker> availableWorkers;
    private WorkerPool nextPool;

    public WorkerPool(int poolSize, Class<? extends Worker> workerType) {
        this.availableWorkers = new ConcurrentLinkedQueue<>();
        initializeWorkers(poolSize, workerType);
    }

    private void initializeWorkers(int poolSize, Class<? extends Worker> workerType) {
        try {
            for (int i = 0; i < poolSize; i++) {
                Worker worker = workerType.getDeclaredConstructor().newInstance();
                availableWorkers.add(worker);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка инициализации рабочих", e);
        }
    }

    public void setNextPool(WorkerPool nextPool) {
        this.nextPool = nextPool;
    }

    public void processCar(Car car, InvoiceSystem invoiceSystem) {
        Worker worker = availableWorkers.poll();
        //елсли у нас есть рабочие
        if (worker != null) {
            try {
                // если проблемы которые есть решаются в нашей бригаде
                if (worker.canHandle(car.getCurrentIssues())) {
                    ServiceReport report = worker.performService(car);
                    System.out.println("Проблемы решены: " + report.getServices());
                    invoiceSystem.createInvoice(car,report);
                }
            } finally {
                // возвращаем рабочего в пул
                availableWorkers.add(worker);
                // если проблемы еще остались передаем дальше по пулам
                if (!car.getCurrentIssues().isEmpty() && nextPool != null) {
                    nextPool.processCar(car,invoiceSystem);
                }
            }
            //если есть пулы дальше, а у нас нет рабочих
        } else if (nextPool != null) {
            nextPool.processCar(car,invoiceSystem);
        }
    }
}