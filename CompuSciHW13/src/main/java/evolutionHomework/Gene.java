package evolutionHomework;

public class Gene {

    public static final double MIN_VALUE = 0, MAX_VALUE = 4;

    private GeneType type;
    private double value;
    private double sd;

    public Gene(GeneType type, double value, double sd) {
        this.type = type;
        this.value = value;
        this.sd = sd;
        bound();
    }

    public Gene(GeneType type, double value) {
        this(type, value, 0);
        bound();
    }

    public Gene(Gene other) {
        this.type = other.type;
        this.value = other.value;
        this.sd = other.sd;
        bound();
    }

    private void bound() {
        value = Math.min(MAX_VALUE, Math.max(MIN_VALUE, value));
    }

    public Gene mutatedVersion() {
        return new Gene(type, value + Arena.getRandom().nextGaussian() * sd, sd);
    }

    public GeneType getType() {
        return type;
    }

    public double getValue() {
        return value;
    }
}
