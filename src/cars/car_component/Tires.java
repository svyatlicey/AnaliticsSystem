package cars.car_component;

public class Tires {
    private int treadDepth = 8; // мм

    public void wear() {
        treadDepth -= Math.random() > 0.7 ? 2 : 1;
    }

    public int getTreadDepth() {
        return treadDepth;
    }
    public void setTreadDepth(int treadDepth) {
        this.treadDepth = treadDepth;
    }
}