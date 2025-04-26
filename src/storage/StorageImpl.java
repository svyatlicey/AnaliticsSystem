package storage;

public interface StorageImpl {
    void writeData(String path,String content) throws StorageException;
    String readData(String path) throws StorageException;
    void deleteData(String path) throws StorageException;


}
