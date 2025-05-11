package cars.states;

import cars.Car;

public class InServiceState implements CarState {
    @Override
    public void ride(Car car, int seconds) {
        System.out.println("[ОШИБКА] Автомобиль " + car.getVIN() + " в сервисе и не может двигаться!");
    }

    @Override
    public void enterService(Car car) {
        System.out.println("Автомобиль уже в сервисе");
    }

    @Override
    public void exitService(Car car) {
        System.out.println("Автомобиль " + car.getVIN() + " покинул сервис");
        car.setCurrentState(new OnRoadState());
    }
}