package autoservice;

import cars.CarIssue;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class ServiceReport {
    private final String workerType;
    private final Set<CarIssue> handledIssues;
    private final double totalCost;

    private ServiceReport(Builder builder) {
        this.workerType = builder.workerType;
        this.handledIssues = Collections.unmodifiableSet(builder.handledIssues);
        this.totalCost = calculateCost(builder.handledIssues);
    }

    public static class Builder {
        private String workerType;
        private final EnumSet<CarIssue> handledIssues = EnumSet.noneOf(CarIssue.class);

        public Builder setWorkerType(String workerType) {
            this.workerType = workerType;
            return this;
        }

        public Builder addServices(Set<CarIssue> issues) {
            this.handledIssues.addAll(issues);
            return this;
        }

        public ServiceReport build() {
            if (workerType == null || workerType.isEmpty()) {
                throw new IllegalStateException("Worker type must be specified");
            }
            return new ServiceReport(this);
        }
    }

    private double calculateCost(Set<CarIssue> issues) {
        // Тарифы для разных видов работ
        final double ENGINE_REPAIR_COST = 15000.0;
        final double ELECTRICAL_REPAIR_COST = 8000.0;
        final double BRAKE_REPAIR_COST = 12000.0;
        final double TIRE_REPLACEMENT_COST = 7000.0;
        final double CLEANING_COST = 3000.0;

        double cost = 0.0;
        for (CarIssue issue : issues) {
            switch (issue) {
                case ENGINE_PROBLEM:
                    cost += ENGINE_REPAIR_COST;
                    break;
                case ELECTRICAL_FAILURE:
                    cost += ELECTRICAL_REPAIR_COST;
                    break;
                case BRAKE_SYSTEM_FAULT:
                    cost += BRAKE_REPAIR_COST;
                    break;
                case TIRE_WEAR:
                    cost += TIRE_REPLACEMENT_COST;
                    break;
                case DIRTY:
                    cost += CLEANING_COST;
                    break;
            }
        }
        return cost;
    }

    // Геттеры
    public String getWorkerType() {
        return workerType;
    }

    public Set<CarIssue> getHandledIssues() {
        return handledIssues;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public Set<CarIssue> getServices() {
        return handledIssues;
    }

    @Override
    public String toString() {
        return String.format(
                "Отчет мастера: %s\nРешенные проблемы: %s\nИтоговая стоимость: %.2f руб.",
                workerType,
                getServices(), // Используем новый метод
                totalCost
        );
    }
}