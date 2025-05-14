// ServiceBox.java
package autoservice;

public class ServiceBox implements AutoCloseable {
    private final int number;
    private boolean occupied;
    private final ServiceBoxPool serviceBoxPool;
    public ServiceBox(int number,ServiceBoxPool serviceBoxPool) {
        this.number = number;
        this.occupied = false;
        this.serviceBoxPool = serviceBoxPool;
    }

    public void occupy() {
        this.occupied = true;
        System.out.println("Бокс " + number + " занят");
    }

    public void release() {
        this.occupied = false;
        serviceBoxPool.releaseBox(this);
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