package storage;

import java.io.*;
import java.nio.file.*;
import java.util.stream.Collectors;

public class SDCard implements StorageDevice {
    private boolean isAvailable = false;

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

    @Override
    public void write(String filePath, String data) throws StorageException {
        checkAvailability();
        Path path = Paths.get(filePath);
        try {
            // Создаем директории если нужно
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            // Записываем данные с перезаписью файла
            Files.write(path, data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Ошибка записи в файл: " + filePath, e);
        }
    }

    @Override
    public String read(String filePath) throws StorageException {
        checkAvailability();
        try {
            return Files.readAllLines(Paths.get(filePath))
                    .stream()
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new StorageException("Ошибка чтения файла: " + filePath, e);
        }
    }

    @Override
    public boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }

    @Override
    public void createDirectory(String directoryPath) throws StorageException {
        checkAvailability();
        try {
            Files.createDirectories(Paths.get(directoryPath));
        } catch (IOException e) {
            throw new StorageException("Ошибка создания директории: " + directoryPath, e);
        }
    }

    @Override
    public void delete(String path) throws StorageException {
        checkAvailability();
        try {
            Files.delete(Paths.get(path));
        } catch (IOException e) {
            throw new StorageException("Ошибка удаления: " + path, e);
        }
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    private void checkAvailability() throws StorageException {
        if (!isAvailable) {
            throw new StorageException("SD карта недоступна");
        }
    }
}