package evolutionHomework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * The basic class for viewing the ecology simulation
 *
 * @author Peter Dong
 *
 */
public class Viewer extends JPanel implements MouseListener, ActionListener {

    private Arena map;
    private LeaderBoard leaderBoard;
    private TimelineBoard timelines;
    private HistogramBoard histogramBoard;
    private boolean paused;
    /**
     * This determines the size of the bar along the bottom of the view.
     */
    private static final int SIZE_OF_BAR = 20;

    /**
     * If I don't have this, Eclipse will yell at me. It is not used.
     */
    private static final long serialVersionUID = -4924678870492657424L;
    private static Map<Integer, List<Animal>> animalsToAdd = new HashMap<>();

    private int currentDay = 0;

    static void queAddOnDay(int animalCount, Class<? extends Animal> type, int day) throws InstantiationException, IllegalAccessException {
        List<Animal> animals = new ArrayList<>();

        if (animalsToAdd.containsKey(day)) {
            animals = animalsToAdd.get(day);
        }

        for (int i = 0; i < animalCount; ++i) {
            Animal newAnimal = Animal.makeRandomAnimal(type);
            newAnimal.randAge();
            animals.add(newAnimal);
        }

        animalsToAdd.put(day, animals);
    }

    public int getDay() {
        return currentDay;
    }

    public Season getSeason() {
        int currentDayInYear = currentDay % Animal.YEAR;

        if (currentDayInYear < Animal.YEAR / 4) {
            return Season.WINTER;
        } else if (currentDayInYear < Animal.YEAR / 4 * 2) {
            return Season.SPRING;
        } else if (currentDayInYear < Animal.YEAR / 4 * 2) {
            return Season.SUMMER;
        } else {
            return Season.FALL;
        }
    }

    /**
     * @param mymap - the Arena to be used
     * @param printout - If this is true, the readout each turn will be printed
     * to the system console.
     */
    public Viewer(Arena mymap, double timeIncrement) {

        currentDay = 0;
        paused = false;

        addMouseListener(this);
        map = mymap;
        map.setViewer(this);
        setSize(map.getXSize(), map.getYSize() + SIZE_OF_BAR);
        setBackground(Color.white);

        leaderBoard = new LeaderBoard(mymap);
        timelines = new TimelineBoard(mymap, timeIncrement);
        Genotype geno = map.getAllAnimals().get(0).getGenotype();
        histogramBoard = new HistogramBoard(mymap, geno);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(leaderBoard);
        frame.setSize(leaderBoard.getWidth(), leaderBoard.getHeight());
        frame.setVisible(true);

        JFrame frame2 = new JFrame();
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.add(timelines);
        frame2.setSize(timelines.getWidth(), timelines.getHeight());
        frame2.setVisible(true);

        JFrame frame3 = new JFrame();
        frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame3.add(histogramBoard);
        frame3.setSize(histogramBoard.getWidth(), histogramBoard.getHeight());
        frame3.setVisible(true);
    }

    /**
     * This allows the user to set a timer so that the Arena takes a turn
     * automatically at regular intervals.
     *
     * @param increment - the amount of time, in milliseconds, between each turn
     */
    public void setTimer(int increment) { // in milliseconds
        Timer time = new Timer(increment, this);
        time.addActionListener(leaderBoard);
        time.addActionListener(histogramBoard);
        time.addActionListener(timelines);
        time.start();
    }

    public void pause() {
        paused = true;
    }

    public void unpause() {
        paused = false;
    }

    public void togglePause() {
        paused = !paused;
    }

    /**
     * This is the actual heart of the function, called whenever an action is
     * received.
     */
    private void doStuff() {
        if (!paused) {
            if (map.doTurn()) {
                repaint();
                currentDay++;
                addEntities();
            } else {
                close();
            }
        }
    }
    
    private  void addEntities(){
        if(animalsToAdd.containsKey(getDay())){
            for(Animal animal: animalsToAdd.get(getDay())){
                map.addRandomAnimal(animal);
            }
        }
    }

    private void close() {
        map.close();
        java.awt.Window w = SwingUtilities.getWindowAncestor(this);
        WindowEvent wev = new WindowEvent(w, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        myPaint(g);
    }

    /**
     * This determines how far from the bottom the text should print.
     */
    private static final int DISTANCE_FROM_EDGE = 25;
    /**
     * This is how far the text starts from the edge of the bar.
     */
    private static final int DISTANCE_FROM_LEFT_EDGE = 10;

    /**
     * This draws the Arena and adds the status bar at the bottom with the
     * Animal count
     *
     * @param g - a Graphics object to draw with
     */
    public void myPaint(Graphics g) {
        map.Draw(g, getLocation().x, getLocation().y);

        g.setColor(Color.black);
        String status = map.countAnimals();
        g.drawString(status, DISTANCE_FROM_LEFT_EDGE, map.getYSize() - DISTANCE_FROM_EDGE);
    }

    // Only the mouseClick event is caught
    @Override
    public void mouseClicked(MouseEvent arg0) {
//        doStuff();
        togglePause();
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
//        pause();
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
//        unpause();
    }

    // This is raised by the Timer object
    @Override
    public void actionPerformed(ActionEvent arg0) {
        doStuff();
    }

    static public void runViewer(Arena mymap, double increment, int timerSpeed) {
        Viewer view = new Viewer(mymap, increment);
        view.setTimer(timerSpeed);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(view);
        frame.setSize(view.getWidth(), view.getHeight());

        frame.setVisible(true);
    }
}
