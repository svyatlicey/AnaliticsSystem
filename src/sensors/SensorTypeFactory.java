package sensors;

import java.util.HashMap;
import java.util.Map;

public class SensorTypeFactory {

    private static final SensorTypeFactory INSTANCE = new SensorTypeFactory();

    private SensorTypeFactory(){}
    private static final Map<String, SensorType> types = new HashMap<>();
    public static SensorTypeFactory getInstance(){
        return INSTANCE;
    }
    public SensorType getType(String name) {

        // если в мапе нет такого же ключа, то добавляем новый элемент <key,SensorType>
        return types.computeIfAbsent(name,
                k -> new SensorType(name)
        );
    }
}
