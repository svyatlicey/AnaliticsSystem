package owner;

import autoservice.Invoice;
import cars.Car;

import java.util.List;

public interface Owner {
    String getName();
    double getBalance();
    void setBalance(double balance);
}
