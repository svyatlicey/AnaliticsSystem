package communication;

import java.util.concurrent.*;
import java.util.*;

public class CANBus implements CommunicationInterface {
    private final Map<String, BusDevice> devices = new ConcurrentHashMap<>();
    private final BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();
    private boolean isConnected = false;

    private static class Message {
        String receiverId;
        String content;
    }

    @Override
    public void send(String receiverId, String message) {
        if (!devices.containsKey(receiverId)) {
            System.err.println("Устройство " + receiverId + " не найдено");
            return;
        }

        Message msg = new Message();
        msg.receiverId = receiverId;
        msg.content = message;
        messageQueue.offer(msg);
        System.out.println("[CAN] Отправлено -> " + receiverId + ": " + message);
    }

    @Override
    public String receive() {
        try {
            Message msg = messageQueue.poll(100, TimeUnit.MILLISECONDS);
            return msg != null ? msg.content : null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public void registerDevice(BusDevice device) {
        devices.put(device.getDeviceId(), device);
        System.out.println("[CAN] Устройство " + device.getDeviceId() + " зарегистрировано");
    }

    @Override
    public void unregisterDevice(String deviceId) {
        devices.remove(deviceId);
        System.out.println("[CAN] Устройство " + deviceId + " отключено");
    }

    @Override
    public boolean isConnected() {
        return isConnected && !devices.isEmpty();
    }

    // Дополнительные методы для управления шиной
    public void connect() {
        isConnected = true;
        System.out.println("[CAN] Шина активирована");
    }

    public void disconnect() {
        isConnected = false;
        devices.clear();
        System.out.println("[CAN] Шина отключена");
    }

    public void processMessages() {
        new Thread(() -> {
            while (isConnected) {
                try {
                    Message msg = messageQueue.take();
                    BusDevice receiver = devices.get(msg.receiverId);
                    if (receiver != null) {
                        receiver.handleMessage(msg.content);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
}