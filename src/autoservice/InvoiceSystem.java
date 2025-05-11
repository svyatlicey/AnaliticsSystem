package autoservice;

import cars.Car;
import owner.CarOwner;
import owner.Owner;

import java.util.*;

public class InvoiceSystem {

    private final List<Invoice> allInvoices = new ArrayList<>();

    public Invoice createInvoice(Car car, ServiceReport report) {
        Invoice invoice = new Invoice(
                car,
                report.getServices(),
                report.getTotalCost()
        );
        allInvoices.add(invoice);

        return invoice;
    }

    public List<Invoice> getInvoicesForOwner(Owner carOwner) {
        List<Invoice> invoices = new ArrayList<>();
        for(Invoice invoice: allInvoices){
            if(invoice.getCar().getOwner().equals(carOwner)){
                invoices.add(invoice);
            }
        }
        return invoices;
    }
    public List<Invoice> getInvoicesForCar(Car car){
        List<Invoice> invoices = new ArrayList<>();
        for(Invoice invoice: allInvoices){
            if(!invoice.isPaid() && invoice.getCar().equals(car)){
                invoices.add(invoice);
            }
        }
        return invoices;
    }

    public List<Car> getCarsForOwner(Owner owner){
        List <Car> cars = new ArrayList<>();
        for(Invoice invoice:allInvoices){
            if(!invoice.isPaid() && invoice.getCar().getOwner().equals(owner)){
                cars.add(invoice.getCar());
            }
        }
        return cars;
    }

    public void markAsPaid(Invoice invoice) {
        invoice.markPaid();
    }

    public List<Invoice> getAllInvoices() {
        return Collections.unmodifiableList(allInvoices);
    }
}