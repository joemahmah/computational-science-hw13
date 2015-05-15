package evolutionHomework;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Sophomore extends Herbivore {

    public Sophomore() {
        super();
        metabolicConsumption = 350;
    }

    @Override
    protected double metabolicConsumption() {
        return metabolicConsumption;
    }

    @Override
    protected double maxEnergyStorage() {

        return metabolicConsumption * 60; //30000 Calories (~20 lbs fat)
    }

    @Override
    protected double maxEnergyStoragePerDay() {
        return metabolicConsumption * 10;
    }

    @Override
    protected int ageOfSexualMaturity() {
        if (!isMale()) {
            return YEAR / 5;
        }

        return 2 * YEAR / 11;
    }

    @Override
    protected int gestationTime() {

        return (getRandom().nextInt(15) + 14); //2 - 4 weeks
    }

    @Override
    protected int litterSize() {

        return (getRandom().nextInt(16) + 15); //15 - 30 not hares
    }

    @Override
    protected double initialEnergy() {
        return metabolicConsumption * (getRandom().nextInt(3) + 5); //5 - 8 days of no food
    }

    @Override
    protected double energyAsFood() {

        return (4 * 3500 + getEnergyReserve());
    }

    @Override
    protected double turnEnergyMax() {

        return metabolicConsumption * 1.25;
    }

    @Override
    public double energyToMate() {
        return metabolicConsumption / 2;
    }

    @Override
    public double energyToMove() {
        return metabolicConsumption / 6;
    }

    @Override
    public double energyToEat() {
        return metabolicConsumption / 4;
    }

    @Override
    public String getName() {
        return "Sophomore";
    }

    @Override
    protected Turn userDefinedChooseMove() {
        List<Animal> others = getArena().getAllAnimals(this);

        if (!isMale()) {
            Animal possibleMate = null;
            for (Animal ani : others) {
                if (checkMateability(ani)) {// && (getArena().getViewer().getSeason() == Season.SPRING)) {
                    if (getDesireLevel(ani) > getDesireLevel(possibleMate)) {
                        possibleMate = ani;
                    }
                }
            }

            if (possibleMate != null && possibleMate.getCell() == getCell()) {
                return new Mate(possibleMate);
            } else if(possibleMate != null){
                return new MoveToward(getCell(), possibleMate.getCell(), true);
            }
        } else if(getCell().getOtherAnimals(this).size() != 0) {
            Animal ani = getCell().getOtherAnimals(this).get(getRandom().nextInt(getCell().getOtherAnimals(this).size()));
            if (checkMateability(ani)) {
                return new Mate(ani);
            }

        }

        if ((getCell().howMuchFood() > energyToEat() && (getConsumptionThisDay() / maxEnergyStoragePerDay()) < 1 && getEnergyReserve() / maxEnergyStorage() < (1.0 - 1.0 / turnEnergyMax())) || getEnergyReserve() / maxEnergyStorage() < .05) {
            return new HerbivoreEat();
        }

        int xPos = getCell().getX();
        int yPos = getCell().getY();
        double foodLeft = 0, foodRight = 0, foodUp = 0, foodDown = 0;

        if (xPos > 0) {
            foodLeft += getArena().getCell(xPos - 1, yPos).howMuchFood();
        }

        if (xPos < getArena().getXDim() - 1) {
            foodRight += getArena().getCell(xPos + 1, yPos).howMuchFood();
        }

        if (yPos > 0) {
            foodUp += getArena().getCell(xPos, yPos - 1).howMuchFood();
        }

        if (yPos < getArena().getYDim() - 1) {
            foodDown += getArena().getCell(xPos, yPos + 1).howMuchFood();
        }

        double[] dirValues = {foodLeft, foodRight, foodUp, foodDown};
        int max = 0;
        for (int i = 0; i < dirValues.length; i++) {
            if (dirValues[i] > dirValues[max]) {
                max = i;
            }
        }

        if (dirValues[max] > energyToEat() + energyToMove()) {
            return new Move(Direction.values()[max]);
        }

        if (Arena.getRandom().nextBoolean()) {
            return new Move(Direction.randomDirection());
        } else {
            return new Sleep();
        }
    }

    @Override
    protected int getNTurns() {
        return turnsPerDay;
    }

    @Override
    protected Color getColor() {
        return Color.RED;
    }

    @Override
    public void Draw(Graphics graph, int x, int y) {
        super.Draw(graph, x, y);
        if (isMale()) {
            float brightness = (float) getGenotype().getGene(GeneType.SPOT_BRIGHTNESS) / 2;
            if (brightness < 0) {
                brightness = 0;
            } else if (brightness > 1) {
                brightness = 1;
            }

            graph.setColor(Color.getHSBColor(230f / 360, 1f, brightness));

            double size = getGenotype().getGene(GeneType.SPOT_SIZE) / 2;
            if (size < 0) {
                size = 0;
            } else if (size > 1) {
                size = 1;
            }
            int sizeX = (int) Math.round(size * getXSize());
            int sizeY = (int) Math.round(size * getYSize());
            graph.fillOval(x + sizeX, y + sizeY, getXSize() - (2 * sizeX - 1), getYSize() - (2 * sizeY - 1));
//            System.err.println(x + sizeX);
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
