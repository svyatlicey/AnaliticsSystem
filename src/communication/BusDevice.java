package communication;

/**
 * Базовый интерфейс для всех устройств, подключаемых к шине
 */
public interface BusDevice {
    /**
     * Обработка входящего сообщения
     * @param message полученные данные
     */
    void handleMessage(String message);

    /**
     * Получение уникального идентификатора устройства
     */
    String getDeviceId();

    /**
     * Подключение к шине
     * @param bus интерфейс коммуникации
     */
    void connectToBus(CommunicationInterface bus);

    /**
     * Отключение от шины
     */
    void disconnectFromBus();
}