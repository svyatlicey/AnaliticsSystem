package analyzer;

import communication.BusDevice;
import storage.StorageDevice;

public interface LogAnalyzer extends BusDevice {
    /**
     * Анализирует сырые данные логов
     * @param rawData данные для анализа
     */
    void analyze(String rawData);

    /**
     * Генерирует текстовый отчет
     * @return строка с результатами анализа
     */
    String generateReport();

    /**
     * Сбрасывает внутреннее состояние анализатора
     */
    void reset();

    /**
     * Сохраняет отчет в хранилище
     * @param storage целевое устройство хранения
     * @param filename имя файла для сохранения
     * @return true если запись прошла успешно
     */
    boolean saveReport(StorageDevice storage, String filename);
}