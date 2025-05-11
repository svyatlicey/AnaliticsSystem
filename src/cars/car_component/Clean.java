package cars.car_component;

public class Clean {
    private int dirtLevel = 0;

    public void wear() {
        dirtLevel += 10 + (int)(Math.random() * 20);
    }

    public int getDirtLevel() {
        return dirtLevel;
    }
    public void setDirtLevel(int dirtLevel){
        this.dirtLevel=dirtLevel;
    }
}