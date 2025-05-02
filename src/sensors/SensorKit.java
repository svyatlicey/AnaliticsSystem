package sensors;

import java.util.ArrayList;
import java.util.List;

public class SensorKit implements Cloneable {
    private final List<Sensor> sensors = new ArrayList<>();
    private final String kitType;

    public SensorKit(String kitType) {
        this.kitType = kitType;
    }

    // Добавление датчиков
    public SensorKit addSensor(Sensor sensor) {
        sensors.add(sensor);
        return this;
    }

    // Получение неизменяемого списка датчиков
    public List<Sensor> getSensors() {
        return new ArrayList<>(sensors);
    }

    // Глубокое клонирование
    @Override
    public SensorKit clone() {
        try {
            SensorKit cloned = (SensorKit) super.clone();
            cloned.sensors.clear();

            // Клонируем каждый датчик
            for (Sensor sensor : this.sensors) {
                cloned.sensors.add(sensor.clone());
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Ошибка клонирования SensorKit", e);
        }
    }

    public String getKitType() {
        return kitType;
    }
}