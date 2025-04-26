import facade.DiagnosticSystemFacade;

public class Main {
    public static void main(String[] args) {
        DiagnosticSystemFacade system = new DiagnosticSystemFacade();

        // Запуск диагностики
        system.startDiagnosticSession(10);

        // Сохранение результатов
        system.saveResults("diagnostics/report.txt");

        // Вывод отчета
        system.printReport();

        // Завершение работы
        system.shutdownSystem();
    }
}