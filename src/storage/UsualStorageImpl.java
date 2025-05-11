package storage;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class UsualStorageImpl implements StorageImpl {
    private boolean isAvailable = false;


    @Override
    public void writeData(String filePath, String content) throws StorageException {
        Path path = Paths.get(filePath);
        if (path.getParent() != null && !Files.exists(path.getParent())) {
            throw new StorageException("Директория не существует: " + path.getParent());
        }
        try {
            Files.write(
                    path,
                    content.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND,
                    StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new StorageException("Ошибка записи в файл: " + filePath, e);
        }
    }

    @Override
    public String readData(String path) throws StorageException {
        try {
            return Files.readAllLines(Paths.get(path))
                    .stream()
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new StorageException("Ошибка чтения файла: " + path, e);
        }
    }

    @Override
    public void deleteData(String path) throws StorageException {
        try {
            Files.delete(Paths.get(path));
        } catch (IOException e) {
            throw new StorageException("SD delete error: " + path, e);
        }
    }
}