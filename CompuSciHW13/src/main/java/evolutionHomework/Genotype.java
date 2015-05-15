package evolutionHomework;

import java.util.ArrayList;
import java.util.List;

public class Genotype {

    private GeneSet fatherGenes;
    private GeneSet motherGenes;

    public Genotype(GeneSet fatherGenes, GeneSet motherGenes) {
        this.fatherGenes = fatherGenes;
        this.motherGenes = motherGenes;
    }

    protected GeneSet getFatherGenes() {
        return fatherGenes;
    }

    protected GeneSet getMotherGenes() {
        return motherGenes;
    }

    public List<GeneType> getGeneTypes() {
        List<GeneType> response = new ArrayList<>();

        for (Gene gene : fatherGenes.allGenesInOrder()) {
            response.add(gene.getType());
        }

        return response;
    }

    public double getGene(GeneType trait) {
        for (int i = 0; i < fatherGenes.allGenesInOrder().size(); ++i) {
            if (fatherGenes.allGenesInOrder().get(i).getType() == trait) {
                return fatherGenes.allGenesInOrder().get(i).getValue() + motherGenes.allGenesInOrder().get(i).getValue();
            }
        }
        throw new RuntimeException("Attempted to access a trait which does not exist!");
    }

    public GeneSet meiosis() {
        List<Gene> father = getFatherGenes().allGenesInOrder();
        List<Gene> mother = getMotherGenes().allGenesInOrder();

        List<Gene> finalList = new ArrayList<>();

        for (int i = 0; i < father.size(); ++i) {
            if (Arena.getRandom().nextBoolean()) {
                finalList.add(father.get(i).mutatedVersion());
            } else {
                finalList.add(mother.get(i).mutatedVersion());
            }
        }

        return new GeneSet(finalList);
    }

}
