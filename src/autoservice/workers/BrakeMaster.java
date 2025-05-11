// BrakeMaster.java
package autoservice.workers;

import autoservice.ServiceReport;
import cars.Car;
import cars.CarIssue;
import cars.car_component.Brakes;

import java.util.EnumSet;
import java.util.Set;

public class BrakeMaster extends Worker {

    private final Set<CarIssue> handledIssues = EnumSet.of(
            CarIssue.BRAKE_SYSTEM_FAULT
    );

    @Override
    public boolean canHandle(Set<CarIssue> issues) {
        return issues.stream().anyMatch(handledIssues::contains);
    }

    @Override
    public ServiceReport performService(Car car) {
        Set<CarIssue> handled = EnumSet.noneOf(CarIssue.class);
        Brakes brakes = car.getBrakes();
        if (car.getCurrentIssues().contains(CarIssue.BRAKE_SYSTEM_FAULT)) {
            System.out.println("Ремонт тормозной системы...");

            if(brakes.getWearLevel() > 80) {
                brakes.setWearLevel(20);
                System.out.println("[Ремонт] Тормоза полностью заменены для " + car.getVIN());
            }

            car.removeIssue(CarIssue.BRAKE_SYSTEM_FAULT);
            handled.add(CarIssue.BRAKE_SYSTEM_FAULT);
        }

        return new ServiceReport.Builder()
                .setWorkerType("Мастер тормозов")
                .addServices(handled)
                .build();
    }

    @Override
    public Set<CarIssue> getHandledIssues() {
        return handledIssues;
    }

}