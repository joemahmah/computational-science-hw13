package evolutionHomework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

public class Histogram extends JPanel {

	private static final long serialVersionUID = 1L;
	private GeneType gene;
	private int size;
	private Color color;
	private List<Integer> bins = new ArrayList<>();
	private double min = Double.MAX_VALUE;
	private double max = Double.MIN_VALUE;
	
	private double yMax = 0;
	private double xMin = Double.MAX_VALUE;
	private double xMax = Double.MIN_VALUE;
	
	private static final double BORDER = .05;
	
	public Histogram(GeneType gene, int histoSize, Color color) {
		this.gene = gene;
		this.size = histoSize;
		this.color = color;
		for (int i = 0; i <= size; ++i) {
			bins.add(0);
		}
	}

	@Override
	public void paint(Graphics arg0) {
		super.paint(arg0);
		
		Rectangle grid = new Rectangle((int)(BORDER * getWidth()), (int)(BORDER * getHeight()), (int)(getWidth() * (1 - 2 * BORDER)),
				(int)(getHeight() * (1 - 4 * BORDER)));
		
		drawHisto(arg0, grid);
		drawAxes(arg0, grid);
	}
	
	public void updateBins(List<Double> numbers) {
		Collections.sort(numbers);
		bins.clear();
		for (int i = 0; i <= size; ++i) {
			bins.add(0);
		}

		min = numbers.get(0);
		max = numbers.get(numbers.size() - 1);
		
		if (min < xMin) {
			xMin = min;
		}
		if (max > xMax) {
			xMax = max;
		}
		
		double increment = (max - min) / size;
		double lbound = min;
		int binNum = 0;
		for (Double num : numbers) {
			while (num > lbound + increment) {
				lbound += increment;
				++binNum;
			}
			if (lbound > max) {
				binNum = size;
			}
			bins.set(binNum, bins.get(binNum) + 1);
		}
	}
	

	private void drawHisto(Graphics arg0, Rectangle grid) {
		double increment = grid.getWidth() / (size + 1);
		double lbound = grid.getMinX();
		double maxHeight = Collections.max(bins);
//		if (maxHeight > yMax) {
			yMax = maxHeight;
//		}
		
		arg0.setColor(color);
		
		for (int i = 0; i < bins.size(); ++i) {
			double height = bins.get(i) / yMax * grid.getHeight();
			arg0.fillRect((int)lbound, (int)Math.ceil(grid.getMaxY() - height), (int)Math.round(increment), (int)height);
			lbound += increment;
		}
	}
	
	static private NumberFormat df = new DecimalFormat("0.#");
	static private int AXIS_TITLE_OFFSET = 30;
	static private int AXIS_LABEL_OFFSET = 12;
	static private int WIDTH_OF_LETTER = 4;
	
	private void drawAxes(Graphics arg0, Rectangle grid) {
		double binMax = yMax;
		double increment = increment(binMax);
		arg0.setColor(Color.BLACK);
		arg0.drawLine((int)grid.getMinX(), (int)grid.getMinY(), (int)grid.getMinX(), (int)grid.getMaxY());
		
		for (double counter = 0; counter < binMax; counter += increment) {
			int positionY = (int)(grid.getMaxY() - counter / binMax * grid.getHeight());
			arg0.drawString(Integer.toString((int)counter), (int)grid.getMinX(), positionY);
		}
		
		int wordWidth = gene.toString().length() * WIDTH_OF_LETTER;
		arg0.drawLine((int)grid.getMinX(), (int)grid.getMaxY(), (int)grid.getMaxX(), (int)grid.getMaxY());
		arg0.drawString(gene.toString(), (int)(grid.getWidth() / 2 - wordWidth / 2), (int)grid.getMaxY() + AXIS_TITLE_OFFSET);
		
		double xInc = (xMax - xMin) / size;		
		for (double counter = xMin; counter < xMax; counter += xInc) {
			int positionX = (int)((counter - xMin) / (xMax - xMin) * grid.getWidth() + grid.getMinX());
			arg0.drawString(df.format(counter), positionX, (int)grid.getMaxY() + AXIS_LABEL_OFFSET);
		}
				
	}
	
	private double increment(double max) {
		if (max == 0) {
			return 0;
		}
		int exponent = (int)Math.floor(Math.log10(max));
		double base = Math.pow(10, exponent);
		int mantissa = (int)(max / base);
		
		if (mantissa > 5) {
			return base;
		} else {
			return (double)base / 5;
		}
	}

}
