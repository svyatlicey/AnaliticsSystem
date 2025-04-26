package storage;

import java.io.IOException;
import java.nio.file.*;

public class SDCard extends StorageDevice {
    private boolean isAvailable = false;

    public SDCard(StorageImpl impl) {
        super(impl);
    }

    @Override
    public boolean isAvailable() {
        // Проверяем доступность при каждом вызове
        try {
            checkAvailability();
            return true;
        } catch (StorageException e) {
            return false;
        }
    }

    @Override
    public boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }

    @Override
    public void createDirectory(String dirPath) throws StorageException {
        checkAvailability();
        try {
            Files.createDirectories(Paths.get(dirPath));
        } catch (IOException e) {
            throw new StorageException("Ошибка создания директории: " + dirPath, e);
        }
    }

    private void checkAvailability() throws StorageException {
        try {
            // Проверка доступности через пробную запись
            Path testFile = Paths.get("sd_card_test.tmp");
            Files.write(testFile, "test".getBytes());
            Files.delete(testFile);
            isAvailable = true;
        } catch (IOException e) {
            isAvailable = false;
            throw new StorageException("SD карта недоступна", e);
        }
    }
    @Override
    public void initialize() throws StorageException {
        try {
            // Проверяем возможность записи в корневую директорию
            Path testFile = Paths.get("sd_card_test.tmp");
            Files.write(testFile, "test".getBytes());
            Files.delete(testFile);
            isAvailable = true;
        } catch (IOException e) {
            throw new StorageException("Ошибка инициализации SD карты", e);
        }
    }
}