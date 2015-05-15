package evolutionHomework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

public class HistogramBoard extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final int EACH_X_SIZE = 300;
	private static final int EACH_Y_SIZE = 300;
	private static final int N_BINS = 10;
	private static final double WINDOW_SCALE = 1.1;
	
	private Arena arena;
	
	private Map<GeneType, Histogram> histograms = new HashMap<>();
	
	public HistogramBoard(Arena arena, Genotype geno) {
		this.arena = arena;
		List<Gene> allGenes = geno.getFatherGenes().allGenesInOrder();
		int width = EACH_X_SIZE * (allGenes.size() / 2 + 1);

		setSize((int)(width * WINDOW_SCALE), (int)(2 * EACH_Y_SIZE * WINDOW_SCALE));
		setBackground(Color.WHITE);
		
		double hue = 0.15;
		final double hueChange = .3;
		
		for (Gene gene : allGenes) {
			histograms.put(gene.getType(), new Histogram(gene.getType(), N_BINS, Color.getHSBColor((float)hue, .5f, .5f)));
			hue += hueChange;
		}
		
		for (Histogram histo : histograms.values()) {
			histo.setPreferredSize(new Dimension(EACH_X_SIZE, EACH_Y_SIZE));
			add(histo);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		update();
		repaint();
	}
	
	private void update() {
		List<Animal> animals = arena.getAllAnimals();
		Map<GeneType, List<Double>> map = new HashMap<>();
		
		for (Animal ani : animals) {
			for (GeneType type : ani.getGenotype().getGeneTypes()) {
				if (map.containsKey(type)) {
					map.get(type).add(ani.getGenotype().getGene(type));
				} else {
					map.put(type, new ArrayList<Double>());
				}
			}
		}
		
		for (GeneType type : map.keySet()) {
			histograms.get(type).updateBins(map.get(type));
		}
	}
}
