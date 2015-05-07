package evolutionHomework;

public class Gene {

	private GeneType type;
	private double value;
	private double sd;

	public Gene(GeneType type, double value, double sd) {
		this.type = type;
		this.value = value;
		this.sd = sd;
	}
	
	public Gene(GeneType type, double value) {
		this(type, value, 0);
	}
	
	public Gene(Gene other) {
		this.type = other.type;
		this.value = other.value;
		this.sd = other.sd;
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
