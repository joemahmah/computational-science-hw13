package evolutionHomework;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Guppy extends Herbivore {

	@Override
	protected double metabolicConsumption() {
		return 1;
	}

	@Override
	protected double maxEnergyStorage() {
		return 1;
	}

	@Override
	protected int ageOfSexualMaturity() {
		return 1;
	}

	@Override
	protected int gestationTime() {
		return 1;
	}

	@Override
	protected int litterSize() {
		return 1;
	}

	@Override
	protected double initialEnergy() {
		return 1;
	}

	@Override
	protected double energyAsFood() {
		return 1;
	}

	@Override
	protected double dailyEnergyMax() {
		return 1;
	}

	@Override
	public double energyToMate() {
		return 1;
	}

	@Override
	public double energyToMove() {
		return 1;
	}

	@Override
	public double energyToEat() {
		return 1;
	}

	@Override
	public String getName() {
		return "Guppy";
	}

	@Override
	protected Turn userDefinedChooseMove() {
		List<Animal> others = getCell().getOtherAnimals(this);
		for (Animal ani : others) {
			if (checkMateability(ani)) {
				return new Mate(ani);
			}
		}

		if (getCell().howMuchFood() > 1) {
			return new HerbivoreEat();
		} else {
			return new Move(Direction.randomDirection());
		}
	}

	@Override
	protected int getNTurns() {
		return 1;
	}

	@Override
	protected Color getColor() {
		return Color.RED;
	}

	@Override
	public void Draw(Graphics graph, int x, int y) {
		super.Draw(graph, x, y);
		if (isMale()) {
			float brightness = (float)getGenotype().getGene(GeneType.SPOT_BRIGHTNESS) / 2;
			if (brightness < 0) {
				brightness = 0;
			} else if (brightness > 1) { 
				brightness = 1; 
			}

			graph.setColor(Color.getHSBColor(295f / 360, 1f, brightness));

			double size = getGenotype().getGene(GeneType.SPOT_SIZE) / 2;
			if (size < 0) {
				size = 0;
			} else if (size > 1) {
				size = 1;
			}
			int sizeX = (int)Math.round(size * getXSize());
			int sizeY = (int)Math.round(size * getYSize());
			graph.fillOval(x + sizeX, y + sizeY, getXSize() - (2 * sizeX - 1), getYSize() - (2 * sizeY - 1));
		}
	}

	static private final double INITIAL_SPOT_SIZE = .5; // Let 1 be the maximum size
	static private final double INITIAL_SPOT_BRIGHTNESS = .5; // Let 1 be the maximum brightness
	static private final double SPOT_SIZE_SD = .1;
	static private final double SPOT_BRIGHTNESS_SD = .1;

	@Override
	protected void randomGenes(List<Gene> genes) {
		genes.add(new Gene(GeneType.SPOT_SIZE, INITIAL_SPOT_SIZE, SPOT_SIZE_SD));
		genes.add(new Gene(GeneType.SPOT_BRIGHTNESS, INITIAL_SPOT_BRIGHTNESS, SPOT_BRIGHTNESS_SD));
	}


}
