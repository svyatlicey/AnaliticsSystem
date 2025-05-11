package autoservice.workers;

import autoservice.ServiceReport;
import cars.Car;
import cars.CarIssue;
import cars.car_component.Tires;

import java.util.EnumSet;
import java.util.Set;

public class TireMaster extends Worker {

    private final Set<CarIssue> handledIssues = EnumSet.of(
            CarIssue.TIRE_WEAR
    );

    @Override
    public boolean canHandle(Set<CarIssue> issues) {
        return issues.stream().anyMatch(handledIssues::contains);
    }

    @Override
    public ServiceReport performService(Car car) {
        Set<CarIssue> handled = EnumSet.noneOf(CarIssue.class);
        Tires tires = car.getTires();

        if (car.getCurrentIssues().contains(CarIssue.TIRE_WEAR)) {
            System.out.println("Замена изношенных шин...");

            if(tires.getTreadDepth() < 2) {
                tires.setTreadDepth(8);
                System.out.println("[Ремонт] Шины заменены для " + car.getVIN());
            }

            car.removeIssue(CarIssue.TIRE_WEAR);
            handled.add(CarIssue.TIRE_WEAR);
        }

        return new ServiceReport.Builder()
                .setWorkerType("Шиномонтажник")
                .addServices(handled)
                .build();
    }

    @Override
    public Set<CarIssue> getHandledIssues() {
        return handledIssues;
    }
}