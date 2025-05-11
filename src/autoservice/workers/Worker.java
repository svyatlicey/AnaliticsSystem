package autoservice.workers;

import autoservice.ServiceReport;
import cars.Car;
import cars.CarIssue;

import java.util.Set;

public abstract class Worker {
    private WorkerPool pool;

    public final void setPool(WorkerPool pool) {
        this.pool = pool;
    }

    protected final WorkerPool getPool() {
        return pool;
    }


    public abstract boolean canHandle(Set<CarIssue> issues);
    public abstract ServiceReport performService(Car car);

    public abstract Set<CarIssue> getHandledIssues();
}
