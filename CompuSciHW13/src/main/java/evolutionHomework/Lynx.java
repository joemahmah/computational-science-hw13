/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionHomework;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mhrcek
 */
public class Lynx extends Carnivore {

    public Lynx() {
        super();
        metabolicConsumption = 300;
        turnsPerDay = 18;
    }

    @Override
    protected double metabolicConsumption() {
        return metabolicConsumption;
    }

    @Override
    protected double maxEnergyStorage() {
        return metabolicConsumption * 200; //60000 Calories (~20 lbs fat)
    }

    @Override
    protected double maxEnergyStoragePerDay() {
        return metabolicConsumption * 75;
    }

    @Override
    protected int ageOfSexualMaturity() {
        return YEAR; //1 Year Old 
    }

    @Override
    protected int gestationTime() {
        return (getRandom().nextInt(11) + 55); //55 - 65 days
    }

    @Override
    protected int litterSize() {
        return (getRandom().nextInt(3) + 2); //2 - 4 lynx
    }

    @Override
    protected double initialEnergy() {
        return metabolicConsumption * (getRandom().nextInt(3) + 10); //7 - 12 days of no food
    }

    @Override
    protected double energyAsFood() {
        return 1;
    }

    @Override
    protected double turnEnergyMax() {
        return metabolicConsumption * 50;
    }

    @Override
    public double energyToMate() {
        return metabolicConsumption * 1.5;
    }

    @Override
    public double energyToMove() {
        return metabolicConsumption / 6;
    }

    @Override
    public double energyToEat() {
        return metabolicConsumption / 2;
    }

    @Override
    public String getName() {
        return "Lynx";
    }

    @Override
    protected Turn userDefinedChooseMove() {
//        List<Animal> others = getCell().getOtherAnimals(this);
//        List<Animal> nearbyAnimals = getNearbyAnimals();
//        List<Animal> nearbyPrey = new ArrayList<>();
//        List<Animal> nearbyMates = new ArrayList<>();
//
//        for (Animal animal : nearbyAnimals) {
//            if (isPrey(animal)) {
//                nearbyPrey.add(animal);
//            }
//            if (animal instanceof Lynx) {
//                if (checkMateability(animal)) {
//                    nearbyMates.add(animal);
//                }
//            }
//        }
//
//        //Check if can mate
//        for (Animal ani : others) {
//            if (checkMateability(ani) && (getArena().getViewer().getSeason() == Season.WINTER) && getEnergyReserve()/maxEnergyStorage() > .75) {
//                return new Mate(ani);
//            }
//        }
//
//        //Check if can eat now
//        if (others.size() > 0) {
//            for (Animal possiblePrey : others) {
//                if (isPrey(possiblePrey)) {
//                    return new CarnivoreEat(possiblePrey);
//                }
//            }
//        }
//
//        //Check if mate near
//        if (nearbyMates.size() > 0 && getArena().getViewer().getSeason() == Season.WINTER) {
//            return new Move(pathTo(nearbyMates.get(0)));
//        }
//
//        //Check if food near
//        if (nearbyPrey.size() > 0) {
//            return new Move(pathTo(nearbyPrey.get(0)));
//        }

//        if (Arena.getRandom().nextBoolean()) {
            return new Move(Direction.randomDirection());
//        } else {
//            return new Sleep();
//        }
    }

    @Override
    protected int getNTurns() {
        return turnsPerDay;
    }

    @Override
    protected Color getColor() {
//        if (getArena().getViewer() != null) {
//            if (getArena().getViewer().getSeason() == Season.WINTER) {
//                return Color.WHITE;
//            }
//        }
//        return Color.BLUE;
//        if (Arena.getRandom().nextBoolean()) {
        return Color.RED;
//        } else if (Arena.getRandom().nextBoolean()) {
//            return Color.RED;
//        } else {
//            return Color.BLUE;
//        }
    }

    @Override
    protected boolean isPrey(Animal target) {
        return target instanceof Herbivore;
    }

    @Override
    protected void randomGenes(List<Gene> genes) {
        
    }

}
