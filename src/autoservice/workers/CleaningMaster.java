package autoservice.workers;

import autoservice.ServiceReport;
import cars.Car;
import cars.CarIssue;
import java.util.EnumSet;
import java.util.Set;

public class CleaningMaster extends Worker {

    private final Set<CarIssue> handledIssues = EnumSet.of(
            CarIssue.DIRTY
    );

    @Override
    public boolean canHandle(Set<CarIssue> issues) {
        return issues.stream().anyMatch(handledIssues::contains);
    }

    @Override
    public ServiceReport performService(Car car) {
        Set<CarIssue> handled = EnumSet.noneOf(CarIssue.class);

        if (car.getCurrentIssues().contains(CarIssue.DIRTY)) {
            System.out.println("Проводим комплексную очистку...");

            car.getClean().setDirtLevel(0);
            car.removeIssue(CarIssue.DIRTY);
            System.out.println("[Ремонт] Автомобиль " + car.getVIN() + " очищен");

            handled.add(CarIssue.DIRTY);
        }

        return new ServiceReport.Builder()
                .setWorkerType("Мойщик")
                .addServices(handled)
                .build();
    }

    @Override
    public Set<CarIssue> getHandledIssues() {
        return handledIssues;
    }
}