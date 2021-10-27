/*
 * Dis how ballers do...
 */

package trust.LightsOut.NeuralPlay;

import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.SwingUtilities;
import trust.LightsOut.Frame;
import trust.LightsOut.State;
import trust.net.LayerPrint;
import trust.net.Network;

/**
 *
 * @author MijitR
 */
public class Player {
    
    public static final int PLAY_WIDTH = 5, PLAY_HEIGHT = 5, DELAY = 123;
    
    public static final Random RAND = new Random();
    
    private static Frame frame;

    public static final void main(final String[] args) {
        
        SwingUtilities.invokeLater(() -> frame = new Frame(PLAY_WIDTH,PLAY_HEIGHT));
        
        final List<LayerPrint> design
                = new LinkedList<>();
        
        //design.add(new LayerPrint().setSize(25).setNumInputs(25).setActivation(LayerPrint.Activation.SIN));
        design.add(new LayerPrint().setSize(120).setNumInputs(25).setActivation(LayerPrint.Activation.SIN));
        design.add(new LayerPrint().setSize(25).setNumInputs(120).setActivation(LayerPrint.Activation.TANH));
        
        final Network net = new Network(design);
       
        final int numStates = 17000, depth = 3;
        
        //final List<State> _imgs = generateData(numStates, depth);
        
        //train(net, _imgs);
        
        //test(net, _imgs);
        
        freeTrain(net);
        freePlay(net);
        
        /*State toSolve = _imgs.get(50);
        toSolve.print();
        
        final List<Point> solutionPath = toSolve.clickHistory();
        
        for(final Point p : solutionPath) {
            System.out.println("Clicking:: " + p);
            toSolve = toSolve.click(p).print();
            System.out.println(Arrays.toString(toSolve.build()));
        }*/
        
    }
    
    public static final void test(final Network net, final List<State> imgs) {
        /*State startState
                = new State(0,5,5,new int[]{-1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1}, null)
                .click(new Point(3,0))//.click(new Point(1,0))
                .click(new Point(2,1))//.click(new Point(4,1))
                .click(new Point(4,2))//.click(new Point(1,2))
                .click(new Point(4,3))//.click(new Point(2,3))
                .click(new Point(3,4));//.click(new Point(2,4));
        */
        State startState = imgs.get(0);
        while(startState.clickHistory().size() <= 4) {
            startState = imgs.get(RAND.nextInt(imgs.size()));
        }
        System.out.println("SOLVING THIS GUY");
        startState.print();
        
        int loops = 0;
        while(!startState.isSolved() && loops < 10) {
            final float[] firstAnswers = net.operate(startState.build());
            final List<Point> firstClicks = getClicks(firstAnswers);
        
            int itter = 0;
            while(!startState.isSolved() && itter < firstClicks.size()) {
                /*final int decisionIndex
                        = getMax(net.operate(startState.build()));
                final Point toClick =
                    new Point(decisionIndex%PLAY_WIDTH,
                            (decisionIndex-decisionIndex%PLAY_WIDTH)/PLAY_WIDTH);*/
                final Point toClick = firstClicks.get(itter);
                System.out.println("Clicking at Point :: " + toClick);
                startState = startState.click(toClick);
                startState.print();
                itter ++;
            }
            if(!startState.isSolved()) {
                System.out.println("REANALYZING BOARD");
                loops ++;
            }
        }
    }
    
    public static final void train(final Network net, final List<State> imgs) {
        for(int epoc = 0; epoc < 900; epoc ++) {
            Collections.shuffle(imgs);
            float epocErr = 0f;
            for(final State s : imgs) {
                net.operate(s.build());
                net.train(s.buildPath());
                epocErr += net.err();
            }
            epocErr /= imgs.size();
            if(epoc % 5 == 0) {
                System.out.println("Epoc: " + epoc);
                System.out.println(epocErr);
            }
        }
    }
    
    public static final List<State> generateData(
            final int numStates, final int maxDepth
    ) {
        State startState
                = new State(0,5,5,new int[]{-1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1}, null);
        
        final List<State> myDat = new LinkedList<>();
        myDat.add(startState);
        
        final List<Integer> heightList = new ArrayList<>();
        for(int i = 0; i < PLAY_HEIGHT; i ++) {
            heightList.add(i);
        }
        
        while(myDat.size()<numStates) {
            startState
                = new State(0,5,5,new int[]{-1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1}, null);
            for(int d = 0; d < maxDepth; d ++) {
                //myDat.add(startState);
                Collections.shuffle(heightList);
                for(int yPot = 0; yPot < heightList.size(); yPot ++) {
                    final int y = heightList.get(yPot);
                    startState = startState.click(
                            new Point(RAND.nextInt(PLAY_WIDTH),y)
                    );
                    if(d==0 && !myDat.contains(startState)) {
                        myDat.add(startState);
                    }
                }
                if(!myDat.contains(startState)) {
                    myDat.add(startState);
                }
            }
        }
        return myDat;
    }
    
    public static final List<State> generateData(final int numStates) {
        State startState
                = new State(0,5,5,new int[]{-1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1}, null);
        
        final List<State> myDat = new LinkedList<>();
        OUTER:
        for(int depth = 0; depth < numStates / (PLAY_WIDTH*PLAY_HEIGHT); depth ++) {
            myDat.add(startState);
            for(int i = 0; i < PLAY_WIDTH*PLAY_HEIGHT; i ++) {
                myDat.add(startState.click(new Point(i%PLAY_WIDTH, (i-i%PLAY_WIDTH)/PLAY_WIDTH)));
            }
            startState = startState.click(new Point(RAND.nextInt(PLAY_WIDTH), RAND.nextInt(PLAY_HEIGHT)));
        }
        
        return myDat;
    }
    
    public static int getMax(final float[] answers) {
        int maxDex = -1;
        float max = Float.NEGATIVE_INFINITY;
        for(int i = 0; i < answers.length; i ++) {
            if(answers[i] >= max) {
                max = answers[i];
                maxDex = i;
            }
        }
        return maxDex;
    }
    
    public static List<Point> getClicks(final float[] answers) {
        final List<Point> clicks = new LinkedList<>();
        for(int i = 0; i < answers.length; i ++) {
            if(answers[i]>0d) {
                clicks.add(
                    new Point(
                        i%PLAY_WIDTH, (i-i%PLAY_WIDTH)/PLAY_WIDTH
                    )
                );
            }
        }
        return clicks;
    }
     
    public static final void freeTrain(
            final Network net
    ) {
        /*State startState
                = new State(0,5,5,new int[]{-1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1}, null)
                .click(new Point(3,0)).click(new Point(1,0))
                .click(new Point(2,1)).click(new Point(4,1))
                .click(new Point(4,2)).click(new Point(1,2))
                .click(new Point(4,3)).click(new Point(2,3))
                .click(new Point(3,4)).click(new Point(2,4));*/
        for(int solves  = 0, passes = 0; solves < 30000; ) {
            State startState
                = new State(0,5,5,new int[]{-1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1}, null);
            final int clickCount = 13;
            for(int c = 0; startState.clickHistory().size() < clickCount; c ++) {
                final Point clickedP;
                startState = startState.click(clickedP = 
                        new Point(RAND.nextInt(PLAY_WIDTH),
                        RAND.nextInt(PLAY_HEIGHT))
                );
            }
            
            int loops = 0;
            boolean clicked = true;
            while(!startState.isSolved() && loops < 10 && clicked) {
                final List<Point> decisions
                        = getClicks(net.operate(startState.build()));
                net.train(startState.buildPath());
                clicked = false;
                for(final Point p : decisions) {
                    clicked = true;
                    startState = startState.click(p);
                    //if(startState.clickHistory().contains(p)) {
                    net.operate(startState.build());
                    net.train(startState.buildPath());
                    //}
                    if(startState.isSolved()) {
                        break;
                    }
                }
                //System.out.println(startState.clickHistory().size() + " dist");
                loops ++;
            }
            if(startState.isSolved()) {
                solves ++;
                System.out.println("Solves: " + solves);
                System.out.println("Solve ratio: " + (solves / (float) passes));
            } else {
                net.operate(startState.build());
                net.train(startState.buildPath());
                passes ++;
            }
        }
    }
    
    public static final void freePlay(final Network net) {
        for(int solves  = 0, passes = 0; true; ) {
            State startState
                = new State(0,5,5,new int[]{-1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1,
                                -1,-1,-1,-1,-1}, null);
            final int clickCount = 13;
            for(int c = 0; startState.clickHistory().size() < clickCount; c ++) {
                final Point clickedP;
                startState = startState.click(clickedP =
                        new Point(RAND.nextInt(PLAY_WIDTH),
                        RAND.nextInt(PLAY_HEIGHT))
                );
            }
            
            int loops = 0;
            while(!startState.isSolved() && loops < 15) {
                final List<Point> decisions
                        = getClicks(net.operate(startState.build()));
                //net.train(startState.buildPath());
                for(final Point p : decisions) {
                    /*if(frame.isWindowUp) {
                        final State sCopy = startState;
                        SwingUtilities.invokeLater(()->
                            frame.display(sCopy)
                        );
                        try {
                            Thread.sleep(DELAY);
                        } catch (final InterruptedException e){ 
                            return;
                        }
                    }*/
                    publish(startState, DELAY);
                    //boolean hit = startState.clickHistory().contains(p);
                    //if(!hit) {
                    //    net.operate(startState.build());
                    //    net.train(startState.buildPath());
                    //}
                    startState = startState.click(p);
                    //net.operate(startState.build());
                    //net.train(startState.buildPath());
                    if(startState.isSolved()) {
                        break;
                    }
                }
                //System.out.println(startState.clickHistory().size() + " dist");
                loops ++;
            }
            if(startState.isSolved()) {
                solves ++;
                System.out.println("Solves: " + solves);
                System.out.println("    Passes: " + passes);
                System.out.println("Solve Ratio: " + (solves / (float) passes));
                
            } else {
                //System.out.println("Abandonded");
                net.operate(startState.build());
                net.train(startState.buildPath());
                passes ++;
            }
            publish(startState,7*DELAY);
        }
    }
    
    private static void publish(final State s, final int del) {
        if(frame != null && frame.isWindowUp) {
            try {
                SwingUtilities.invokeLater(()->
                    frame.display(s)
                );
                Thread.sleep(del);
            } catch (final InterruptedException e){
                
            }
        }
    }
    
}
