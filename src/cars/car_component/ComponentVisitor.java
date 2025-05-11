package cars.car_component;

import cars.Car;
import cars.CarIssue;
import storage.StorageDevice;
import storage.StorageException;
import systems.DiagnosticSystem;

import java.util.EnumSet;
import java.util.Set;

public class ComponentVisitor {
    private Set<CarIssue> issues = EnumSet.noneOf(CarIssue.class);

    public Set<CarIssue> visit(Brakes brakes) {
        if(brakes.getWearLevel() > 80) {
            issues.add(CarIssue.BRAKE_SYSTEM_FAULT);
        }
        return issues;
    }

    public Set<CarIssue> visit(Tires tires) {
        if(tires.getTreadDepth() < 2) {
            issues.add(CarIssue.TIRE_WEAR);
        }
        return issues;
    }

    public Set<CarIssue> visit(Clean clean) {
        if(clean.getDirtLevel() > 50) {
            issues.add(CarIssue.DIRTY);
        }
        return issues;
    }

    public void visit(DiagnosticSystem system, Car car) {
        StorageDevice storage = system.getStorage();
        String filename = "diagnostics/" + car.getModel().replace(" ", "_")
                + car.getVIN() + "report.txt";

        try {
            if (storage.exists(filename)) {
                String report = storage.read(filename);
                processReport(report, car);
                storage.delete(filename);
                System.out.println("[Visitor] Отчет диагностики обработан и удален: " + filename);
            }

            system.getLogAnalyzer().reset();
            System.out.println("[Visitor] Логи диагностики сброшены для " + car.getVIN());

        } catch (StorageException e) {
            System.err.println("Ошибка обработки отчетов: " + e.getMessage());
        }
    }

    private void processReport(String report, Car car) {
        Set<CarIssue> reportIssues = EnumSet.noneOf(CarIssue.class);

        // Разделяем записи по строкам
        String[] records = report.split("\n");
        for (String record : records) {
            // Парсим тип контроллера
            if (record.contains("\"error\"")) {
                if(record.contains("\"engine_microcontroller\"")){
                    reportIssues.add(CarIssue.ENGINE_PROBLEM);
                }else if(record.contains("\"power_controller\"")){
                    reportIssues.add(CarIssue.ELECTRICAL_FAILURE);
                }

            }
        }

        // Добавляем уникальные проблемы
        for (CarIssue issue : reportIssues) {
            if (!car.getCurrentIssues().contains(issue)) {
                car.addIssue(issue);
                System.out.println("[Visitor] Выявлена проблема из отчета: " + issue);
            }
        }
    }

    public Set<CarIssue> getDetectedIssues() {
        return issues;
    }

    public void reset() {
        issues.clear();
    }
}