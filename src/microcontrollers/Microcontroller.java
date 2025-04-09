package microcontrollers;

import communication.BusDevice;
import sensors.Sensor;
import java.util.List;
public interface Microcontroller extends BusDevice {

    /**
     * Добавление датчика в систему мониторинга
     * @param sensor подключаемый датчик
     */
    void addSensor(Sensor sensor);

    /**
     * Удаление датчика из системы
     * @param sensorType идентификатор датчика
     */
    void removeSensor(String sensorType);

    /**
     * Получение списка подключенных датчиков
     */
    List<Sensor> getConnectedSensors();

    /**
     * Основной цикл обработки данных
     */
    void processSensorData();

    /**
     * Установка пороговых значений для датчика
     * @param sensorType тип датчика (например, "Temperature")
     * @param min минимальное допустимое значение
     * @param max максимальное допустимое значение
     */
    void setThresholds(String sensorType, double min, double max);

    /**
     * Обработка критических событий
     * @param sensor датчик с аномальными показаниями
     * @param value зафиксированное значение
     */
    void handleCriticalEvent(Sensor sensor, double value);
}