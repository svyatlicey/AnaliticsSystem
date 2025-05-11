// ServiceBox.java
package autoservice;

public class ServiceBox implements AutoCloseable {
    private final int number;
    private boolean occupied;

    public ServiceBox(int number) {
        this.number = number;
        this.occupied = false;
    }

    public void occupy() {
        this.occupied = true;
        System.out.println("Бокс " + number + " занят");
    }

    public void release() {
        this.occupied = false;
        System.out.println("Бокс " + number + " освобожден");
    }

    public int getNumber() {
        return number;
    }

    public boolean isOccupied() {
        return occupied;
    }

    @Override
    public void close() {
        release();
    }
}