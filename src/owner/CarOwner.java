package owner;

import autoservice.Invoice;
import cars.Car;

import java.util.ArrayList;
import java.util.List;

public class CarOwner implements Owner{
    private final String name;
    double balance;
    public CarOwner(String name){
        balance = 1000000;
        this.name = name;
    }
    @Override
    public String getName(){
        return name;
    }

    @Override
    public double getBalance(){
        return balance;
    }

    @Override
    public void setBalance(double balance){
        this.balance = balance;
    }

}
