package storage;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class StorageDevice {
    protected StorageImpl impl;

    public StorageDevice(StorageImpl impl) {
        this.impl = impl;
    }

    // Базовые методы (делегированы реализации)
    public void write(String path, String data) throws StorageException {
        Path filePath = Paths.get(path);

        // 1. Проверяем существование родительской директории
        if (filePath.getParent() != null) {
            String parentDir = filePath.getParent().toString();

            if (!exists(parentDir)) {
                // 2. Создаем директорию рекурсивно
                createDirectory(parentDir);
            }
        }

        // 3. Вызываем реализацию
        impl.writeData(path, data);
    }

    public String read(String path) throws StorageException {
        return impl.readData(path);
    }

    public void delete(String path) throws StorageException {
        impl.deleteData(path);
    }

    public abstract boolean isAvailable();

    // Дополнительные методы (реализованы в абстракции)
    public abstract boolean exists(String path);

    public abstract void createDirectory(String dirPath) throws StorageException;

    public abstract void initialize() throws StorageException;

    // Метод для изменения реализации
    public void switchImplementation(StorageImpl newImpl) {
        this.impl = newImpl;
    }
}