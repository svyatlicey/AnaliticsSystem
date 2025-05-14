package autoservice.workers;

import autoservice.ServiceReport;
import cars.Car;
import cars.CarIssue;
import storage.StorageDevice;
import storage.StorageException;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DiagnosticMaster extends Worker {

    private final Set<CarIssue> handledIssues = EnumSet.of(
            CarIssue.ENGINE_PROBLEM,
            CarIssue.ELECTRICAL_FAILURE
    );

    // Паттерны для поиска ошибок
    private static final Pattern ENGINE_ERROR_PATTERN =
            Pattern.compile("\"error\":\"engine_microcontroller\"");
    private static final Pattern POWER_ERROR_PATTERN =
            Pattern.compile("\"error\":\"power_controller\"");

    @Override
    public boolean canHandle(Set<CarIssue> issues) {
        return issues.stream().anyMatch(handledIssues::contains);
    }

    @Override
    public ServiceReport performService(Car car) {
        Set<CarIssue> handled = EnumSet.noneOf(CarIssue.class);
        StorageDevice storage = car.getDiagnosticSystem().getStorage();
        String filePath = buildLogFilePath(car);

        try {
            if (car.getCurrentIssues().contains(CarIssue.ENGINE_PROBLEM)) {
                System.out.println("Ремонт двигателя...");
                cleanLogs(storage, filePath, ENGINE_ERROR_PATTERN);
                car.removeIssue(CarIssue.ENGINE_PROBLEM);
                handled.add(CarIssue.ENGINE_PROBLEM);
            }

            if (car.getCurrentIssues().contains(CarIssue.ELECTRICAL_FAILURE)) {
                System.out.println("Ремонт электроники...");
                cleanLogs(storage, filePath, POWER_ERROR_PATTERN);
                car.removeIssue(CarIssue.ELECTRICAL_FAILURE);
                handled.add(CarIssue.ELECTRICAL_FAILURE);
            }
        } catch (StorageException e) {
            System.err.println("Ошибка очистки логов: " + e.getMessage());
        }

        return new ServiceReport.Builder()
                .setWorkerType("Диагност")
                .addServices(handled)
                .build();
    }

    private String buildLogFilePath(Car car) {
        return "diagnostics/"
                + car.getModel()
                + car.getVIN()
                + "_report.txt";
    }

    private void cleanLogs(StorageDevice storage, String filePath, Pattern pattern)
            throws StorageException {

        if (!storage.exists(filePath)) return;

        String content = storage.read(filePath);
        List<String> filteredLines = content.lines()
                .filter(line -> !pattern.matcher(line).find())
                .collect(Collectors.toList());

        storage.write(filePath, String.join("\n", filteredLines));
    }

    @Override
    public Set<CarIssue> getHandledIssues() {
        return handledIssues;
    }
}