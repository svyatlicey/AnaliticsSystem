package microcontrollers;

public class Threshold {
    final double min;
    final double max;

    Threshold(double min, double max) {
        this.min = min;
        this.max = max;
    }

    boolean isCritical(double value) {
        return value < min || value > max;
    }
}
