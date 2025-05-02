package systems.fabric;

import systems.DiagnosticSystem;

public abstract class DiagnosticSystemFactory {

    // Фабричный метод для создания системы диагностики
    public abstract DiagnosticSystem createDiagnosticSystem(String engineType);

    // Общая логика для всех фабрик
    public void prepareSystem() {
        System.out.println("Подготовка диагностического оборудования...");
    }
}