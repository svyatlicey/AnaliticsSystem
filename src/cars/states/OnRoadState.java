package cars.states;

import cars.Car;
import cars.car_component.*;

public class OnRoadState implements CarState {
    @Override
    public void ride(Car car, int seconds) {
        System.out.println("Автомобиль " + car.getVIN() + " начал движение");
        // Существующая логика поездки с износом компонентов
        long endTime = System.currentTimeMillis() + seconds * 1000;

        while(System.currentTimeMillis() < endTime) {
            car.performFullDiagnostic();
            car.getBrakes().wear();
            car.getTires().wear();
            car.getClean().wear();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    public void enterService(Car car) {
        System.out.println("Автомобиль " + car.getVIN() + " направлен в сервис");
        car.setCurrentState(new InServiceState());
    }

    @Override
    public void exitService(Car car) {
        System.out.println("Автомобиль уже на дороге");
    }
}