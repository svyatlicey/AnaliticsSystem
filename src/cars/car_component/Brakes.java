package cars.car_component;

public class Brakes {
    private int wearLevel = 0;

    public void wear() {
        wearLevel += 5 + (int)(Math.random() * 10);
    }

    public int getWearLevel() {
        return wearLevel;
    }
    public void setWearLevel(int wearLevel) {
        this.wearLevel = wearLevel;
    }
}