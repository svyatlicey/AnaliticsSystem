// ServiceBoxPool.java
package autoservice;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServiceBoxPool {
    private final Queue<ServiceBox> availableBoxes;
    private final int totalBoxes;

    public ServiceBoxPool(int totalBoxes) {
        this.totalBoxes = totalBoxes;
        this.availableBoxes = new ConcurrentLinkedQueue<>();
        initializeBoxes();
    }

    private void initializeBoxes() {
        for (int i = 1; i <= totalBoxes; i++) {
            availableBoxes.add(new ServiceBox(i,this));
        }
    }

    public ServiceBox acquireBox() {
        ServiceBox box = availableBoxes.poll();
        if (box != null) {
            box.occupy();
        }
        return box;
    }

    public void releaseBox(ServiceBox box) {
        if (box != null) {
            box.release();
            availableBoxes.add(box);
        }
    }

    public int getAvailableBoxesCount() {
        return availableBoxes.size();
    }

    public int getTotalBoxes() {
        return totalBoxes;
    }
}