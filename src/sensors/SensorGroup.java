package sensors;

import java.util.*;

public class SensorGroup implements Sensor, Iterable<Sensor>{
    private List<Sensor> sensors;
    private String type;
    public SensorGroup(Sensor...sensors){
        if(sensors.length<1){
            throw new IllegalArgumentException("Количество датчиков должно быть больше 1");
        }
        this.sensors = new ArrayList<Sensor>();
        this.type = sensors[0].getType();
        for(Sensor s:sensors){
            if(!s.getType().equals(this.type)){
                throw new IllegalArgumentException(
                        "Несовместимый тип датчика. Ожидается: " + type
                                + ", получен: " + s.getType()
                );
            }else{
                this.sensors.add(s);
            }
        }
    }
    @Override
    public String getType(){
        return this.type;
    }

    @Override
    public void add(Sensor sensor){
        if(sensor.getType().equals(this.type)){
            this.sensors.add(sensor);
        }else {
            throw new IllegalArgumentException(
                    "Несовместимый тип датчика. Ожидается: " + type
                            + ", получен: " + sensor.getType()
            );
        }
    }
    @Override
    public void remove(Sensor sensor){
        sensors.remove(sensor);
    }
    @Override
    public Sensor getSensor(int index){
        return sensors.get(index);
    }
    @Override
    public void calibrate(double offset){
        for(Sensor s:this.sensors){
            s.calibrate(offset);
        }
    }
    @Override
    public double getValue() throws SensorException {
        List<Double> successfulValues = new ArrayList<>();
        List<SensorException> errors = new ArrayList<>();

        // Собираем успешные показания и ошибки
        for (Sensor sensor : sensors) {
            try {
                successfulValues.add(sensor.getValue());
            } catch (SensorException e) {
                errors.add(e);
                System.out.printf("[SensorGroup] Датчик %s вызвал ошибку: %s. Показание игнорируется%n",
                        sensor.getType(), e.getMessage());
            }
        }

        // Обработка случая, когда все датчики вернули ошибку
        if (successfulValues.isEmpty()) {
            throw new SensorException("Все датчики в группе " + type + " вернули ошибки. Последняя ошибка: "
                    + (errors.isEmpty() ? "неизвестна" : errors.get(errors.size()-1).getMessage()));
        }

        // Рассчет среднего значения
        double sum = successfulValues.stream().mapToDouble(Double::doubleValue).sum();
        return sum / successfulValues.size();
    }

    @Override
    public Iterator<Sensor> iterator() {
        return new SensorIterator();
    }

    private class SensorIterator implements Iterator<Sensor> {
        private Deque<Iterator<Sensor>> deque = new ArrayDeque<>();
        private Sensor nextSensor = null;

        public SensorIterator() {
            deque.push(sensors.iterator());
            findNext();
        }

        private void findNext() {
            while (!deque.isEmpty()) {
                Iterator<Sensor> iterator = deque.peek();
                if (iterator.hasNext()) {
                    Sensor sensor = iterator.next();
                    if (sensor instanceof SensorGroup) {
                        deque.push(((SensorGroup) sensor).iterator());
                    } else {
                        nextSensor = sensor;
                        return;
                    }
                } else {
                    deque.pop();
                }
            }
            nextSensor = null;
        }

        @Override
        public boolean hasNext() {
            return nextSensor != null;
        }

        @Override
        public Sensor next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Sensor result = nextSensor;
            findNext();
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
