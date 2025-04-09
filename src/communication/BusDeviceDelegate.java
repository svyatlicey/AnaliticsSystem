package communication;

/**
 * Делегат для реализации логики BusDevice
 */
public class BusDeviceDelegate implements BusDevice {
    private final String deviceId;
    private CommunicationInterface bus;
    private boolean isConnected = false;
    private final MessageHandler messageHandler;

    public BusDeviceDelegate(String deviceId, MessageHandler messageHandler) {
        this.deviceId = deviceId;
        this.messageHandler = messageHandler;
    }

    @Override
    public void handleMessage(String message) {
        messageHandler.onMessageReceived(message);
    }

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public void connectToBus(CommunicationInterface bus) {
        if (this.bus != null) disconnectFromBus();
        this.bus = bus;
        bus.registerDevice(this);
        isConnected = true;
        System.out.println("[BUS] " + deviceId + " подключен к шине");
    }

    @Override
    public void disconnectFromBus() {
        if (bus != null) {
            bus.unregisterDevice(deviceId);
            bus = null;
        }
        isConnected = false;
        System.out.println("[BUS] " + deviceId + " отключен от шины");
    }

    public void send(String receiverId, String message) {
        if (isConnected && bus != null) {
            bus.send(receiverId, message);
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public interface MessageHandler {
        void onMessageReceived(String message);
    }
}