package cars.states;

import cars.Car;

public interface CarState {
    void ride(Car car, int seconds);
    void enterService(Car car);
    void exitService(Car car);
}