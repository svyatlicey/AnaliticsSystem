package autoservice;

import cars.Car;
import cars.CarIssue;
import owner.CarOwner;
import owner.Owner;

import java.time.LocalDate;
import java.util.Set;

public class Invoice {
    private final Car car;
    private final Set<CarIssue> services;
    private final double amount;
    private final LocalDate issueDate;
    private boolean isPaid;

    public Invoice(Car car, Set<CarIssue> services, double amount) {
        this.car = car;
        this.services = Set.copyOf(services);
        this.amount = amount;
        this.issueDate = LocalDate.now();
        this.isPaid = false;
    }

    // Геттеры
    public Car getCar() { return car; }
    public Set<CarIssue> getServices() { return services; }
    public double getAmount() { return amount; }
    public LocalDate getIssueDate() { return issueDate; }
    public boolean isPaid() { return isPaid; }

    public void markPaid() {
        this.isPaid = true;
    }

    @Override
    public String toString() {
        return String.format(
                "Счет #%d\nВладелец: %s\nУслуги: %s\nСумма: %.2f руб.\nСтатус: %s\nДата: %s",
                hashCode(),
                car.getOwner().getName(),
                services,
                amount,
                isPaid ? "Оплачен" : "Не оплачен",
                issueDate
        );
    }
}