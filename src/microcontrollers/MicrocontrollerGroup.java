package microcontrollers;

import communication.CommunicationInterface;
import sensors.Sensor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class MicrocontrollerGroup implements Microcontroller, Iterable<Microcontroller>{
    private List<Microcontroller> microcontrollers;
    public MicrocontrollerGroup(Microcontroller... microcontrollers) {
        if(microcontrollers.length<1){
            throw new IllegalArgumentException("Количество Микроконтроллеров должно быть больше 1");
        }
        this.microcontrollers = new ArrayList<Microcontroller>();
        this.microcontrollers.addAll(List.of(microcontrollers));
    }
    @Override
    public List<Sensor> getConnectedSensors() {
        return microcontrollers.stream()
                .map(Microcontroller::getConnectedSensors)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
    @Override
    public void addSensor(Sensor sensor) {
        for(Microcontroller microcontroller:microcontrollers){
            microcontroller.addSensor(sensor);
        }
    }

    @Override
    public void removeSensor(String sensorType) {
        for(Microcontroller microcontroller:microcontrollers){
            microcontroller.removeSensor(sensorType);
        }
    }
    @Override
    public void processSensorData(){
        for(Microcontroller microcontroller:microcontrollers){
            microcontroller.processSensorData();
        }
    }
    @Override
    public void setThresholds(String sensorType, double min, double max){
        for(Microcontroller microcontroller:microcontrollers){
            microcontroller.setThresholds(sensorType,min,max);
        }
    }
    @Override
    public void handleCriticalEvent(Sensor sensor, double value){
        microcontrollers.getFirst().handleCriticalEvent(sensor,value);
    }
    @Override
    public void add(Microcontroller microcontroller){
        microcontrollers.add(microcontroller);
    }
    @Override
    public void remove(Microcontroller microcontroller){
        microcontrollers.remove(microcontroller);
    }
    @Override
    public void connectToBus(CommunicationInterface bus){
        for(Microcontroller microcontroller:microcontrollers){
            microcontroller.connectToBus(bus);
        }
    }
    @Override
    public void disconnectFromBus(){
        for(Microcontroller microcontroller:microcontrollers){
            microcontroller.disconnectFromBus();
        }
    }
   @Override
   public String getDeviceId(){
        StringBuilder sb = new StringBuilder();
        for(Microcontroller microcontroller:microcontrollers){
            sb.append(microcontroller.getDeviceId());
            sb.append(" ");
        }
        return sb.toString();
   }
   @Override
    public void handleMessage(String message){
        for(Microcontroller microcontroller:microcontrollers){
            microcontroller.handleMessage(message);
        }
   }
   @Override
    public Microcontroller getMicrocontroller(int index) {
        return microcontrollers.get(index);
   }
    @Override
    public Iterator<Microcontroller> iterator() {
        return new MicrocontrollerIterator();
    }

    private class MicrocontrollerIterator implements Iterator<Microcontroller> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < microcontrollers.size();
        }

        @Override
        public Microcontroller next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return microcontrollers.get(currentIndex++);
        }

        @Override
        public void remove() {
            if (currentIndex <= 0) {
                throw new IllegalStateException();
            }
            microcontrollers.remove(--currentIndex);
        }
    }
}
