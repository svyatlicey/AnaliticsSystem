package cars;

import cars.car_component.Brakes;
import cars.car_component.Clean;
import cars.car_component.Tires;
import cars.states.CarState;
import owner.Owner;
import systems.DiagnosticSystem;

import java.util.Set;

public interface Car{
    void performFullDiagnostic();
    String getModel();
    int getProductionYear();
    String getVIN();
    void ride(int seconds);

    Set<CarIssue> getCurrentIssues();
    void addIssue(CarIssue issue);
    void clearIssues();
    void removeIssue(CarIssue issue);
    Brakes getBrakes();
    Tires getTires();
    Clean getClean();
    DiagnosticSystem getDiagnosticSystem();

    void setCurrentState(CarState state);
    CarState getCurrentState();

    void setOwner(Owner owner);
    Owner getOwner();

}