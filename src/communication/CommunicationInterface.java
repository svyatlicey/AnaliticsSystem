package communication;

/**
 * Интерфейс для системы коммуникации между устройствами
 */
public interface CommunicationInterface {
    /**
     * Отправка сообщения конкретному устройству
     * @param receiverId уникальный идентификатор получателя
     * @param message данные для отправки
     */
    void send(String receiverId, String message);

    /**
     * Получение сообщений для текущего устройства
     * @return сообщение или null если очередь пуста
     */
    String receive();

    /**
     * Регистрация устройства в шине
     * @param device подключаемое устройство
     */
    void registerDevice(BusDevice device);

    /**
     * Отключение устройства от шины
     * @param deviceId идентификатор устройства
     */
    void unregisterDevice(String deviceId);

    /**
     * Проверка активности соединения
     */
    boolean isConnected();

    void processMessages();

    void disconnect();

    void connect();
}