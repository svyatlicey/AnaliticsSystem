// Дизельные автомобили
package cars;

import cars.car_component.Brakes;
import cars.car_component.Clean;
import cars.car_component.ComponentVisitor;
import cars.car_component.Tires;
import cars.states.CarState;
import cars.states.OnRoadState;
import owner.Owner;
import systems.DiagnosticSystem;

import java.util.HashSet;
import java.util.Set;

public abstract class DieselCar implements Car{
    protected final String model;
    protected final int year;
    protected final String VIN;
    protected final DiagnosticSystem diagnosticSystem;
    protected Set<CarIssue> currentIssues = new HashSet<>();
    protected final Brakes brakes = new Brakes();
    protected final Tires tires = new Tires();
    protected final Clean clean = new Clean();
    protected Owner owner = null;

    private CarState currentState = new OnRoadState();

    public DieselCar(String model, int year, String VIN, DiagnosticSystem diagnosticSystem) {
        this.model = model;
        this.year = year;
        this.VIN = VIN;
        this.diagnosticSystem = diagnosticSystem;
    }
    @Override
    public void setOwner(Owner owner){
        this.owner = owner;
    }
    @Override
    public Owner getOwner(){
        return owner;
    }

    @Override
    public void ride(int seconds){
        currentState.ride(this,seconds);
    }
    @Override
    public void setCurrentState(CarState state){
        this.currentState = state;
    }
    @Override
    public CarState getCurrentState(){
        return this.currentState;
    }


    @Override
    public Brakes getBrakes(){
        return this.brakes;
    }

    @Override
    public Tires getTires(){
        return this.tires;
    }

    @Override
    public Clean getClean(){
        return this.clean;
    }
    @Override
    public DiagnosticSystem getDiagnosticSystem(){ return this.diagnosticSystem; }

    @Override
    public Set<CarIssue> getCurrentIssues() {
        return new HashSet<>(currentIssues);
    }

    @Override
    public void addIssue(CarIssue issue) {
        currentIssues.add(issue);
    }

    @Override
    public void clearIssues() {
        currentIssues.clear();
    }

    public void performFullDiagnostic(){

    }
    @Override
    public void removeIssue(CarIssue issue){
        if(currentIssues.contains(issue)) {
            currentIssues.remove(issue);
        }
    }
    public abstract void doSomethingDiesel();
    @Override public String getVIN(){return VIN;}
    @Override public String getModel() { return model; }
    @Override public int getProductionYear() { return year; }
}