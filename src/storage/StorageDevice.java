package storage;

/**
 * Интерфейс для устройств хранения данных
 */
public interface StorageDevice {

    /**
     * Инициализирует устройство
     * @throws StorageException при ошибке инициализации
     */
    void initialize() throws StorageException;

    /**
     * Записывает данные в указанный файл
     * @param filePath путь к файлу
     * @param data данные для записи
     * @throws StorageException при ошибке записи
     */
    void write(String filePath, String data) throws StorageException;

    /**
     * Читает данные из файла
     * @param filePath путь к файлу
     * @return содержимое файла
     * @throws StorageException при ошибке чтения
     */
    String read(String filePath) throws StorageException;

    /**
     * Проверяет существование файла/директории
     * @param path путь к объекту
     * @return true если объект существует
     */
    boolean exists(String path);

    /**
     * Создает директорию
     * @param directoryPath путь к директории
     * @throws StorageException при ошибке создания
     */
    void createDirectory(String directoryPath) throws StorageException;

    /**
     * Удаляет файл или директорию
     * @param path путь к объекту
     * @throws StorageException при ошибке удаления
     */
    void delete(String path) throws StorageException;

    /**
     * Проверяет доступность устройства
     */
    boolean isAvailable();
}

/**
 * Исключение для операций с хранилищем
 */
