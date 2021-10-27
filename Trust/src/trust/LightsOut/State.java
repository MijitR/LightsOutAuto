/*
 * Dis how ballers do...
 */

package trust.LightsOut;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author MijitR
 */
public record State(int index, int height, int width,
        int[] state, List<Point> clickHistory) {
    
    private static final List<Point>[] PATTERNS = new List[3];
    
    static {
        PATTERNS[0] = new ArrayList();
        PATTERNS[0].add(new Point(0,0));
        PATTERNS[0].add(new Point(2,0));
        PATTERNS[0].add(new Point(4,0));
        PATTERNS[0].add(new Point(0,1));
        PATTERNS[0].add(new Point(2,1));
        PATTERNS[0].add(new Point(4,1));
        PATTERNS[0].add(new Point(0,3));
        PATTERNS[0].add(new Point(2,3));
        PATTERNS[0].add(new Point(4,3));
        PATTERNS[0].add(new Point(0,4));
        PATTERNS[0].add(new Point(2,4));
        PATTERNS[0].add(new Point(4,4));
        
        PATTERNS[1] = new ArrayList();
        PATTERNS[1].add(new Point(0,0));
        PATTERNS[1].add(new Point(0,2));
        PATTERNS[1].add(new Point(0,4));
        PATTERNS[1].add(new Point(1,0));
        PATTERNS[1].add(new Point(1,2));
        PATTERNS[1].add(new Point(1,4));
        PATTERNS[1].add(new Point(3,0));
        PATTERNS[1].add(new Point(3,2));
        PATTERNS[1].add(new Point(3,4));
        PATTERNS[1].add(new Point(4,0));
        PATTERNS[1].add(new Point(4,2));
        PATTERNS[1].add(new Point(4,4));
        
        PATTERNS[2] = new ArrayList();
        PATTERNS[2].add(new Point(0,1));
        PATTERNS[2].add(new Point(0,2));
        PATTERNS[2].add(new Point(0,3));
        PATTERNS[2].add(new Point(1,0));
        PATTERNS[2].add(new Point(1,2));
        PATTERNS[2].add(new Point(1,4));
        PATTERNS[2].add(new Point(2,0));
        PATTERNS[2].add(new Point(2,1));
        PATTERNS[2].add(new Point(2,3));
        PATTERNS[2].add(new Point(2,4));
        PATTERNS[2].add(new Point(3,0));
        PATTERNS[2].add(new Point(3,2));
        PATTERNS[2].add(new Point(3,4));
        PATTERNS[2].add(new Point(4,1));
        PATTERNS[2].add(new Point(4,2));
        PATTERNS[2].add(new Point(4,3));
    }
    
    public static final int ON_VAL = 1, OFF_VAL = -1;
   
    public static void safeSwap(final int state[],
            final int a, final int b, final int c,
                final int d, final int e) {
        if(a >= 0) {
            if(state[a] == ON_VAL) {
                state[a] = OFF_VAL;
            } else {
                state[a] = ON_VAL;
            }
        }
        if(b >= 0) {
            if(state[b] == ON_VAL) {
                state[b] = OFF_VAL;
            } else {
                state[b] = ON_VAL;
            }
        }
        if(c >= 0) {
            if(state[c] == ON_VAL) {
                state[c] = OFF_VAL;
            } else {
                state[c] = ON_VAL;
            }
        }
        if(d >= 0) {
            if(state[d] == ON_VAL) {
                state[d] = OFF_VAL;
            } else {
                state[d] = ON_VAL;
            }
        }
        if(e >= 0) {
            if(state[e] == ON_VAL) {
                state[e] = OFF_VAL;
            } else {
                state[e] = ON_VAL;
            }
        }
    }
    
    public State(final int index, final int height, final int width,
            final int[] state, final List<Point> clickHistory) {
        assert height*width == state.length;
        this.height = height;
        this.width = width;
        this.state = Arrays.copyOf(state, state.length);
        this.index = index;
        this.clickHistory = new LinkedList<>();
        if(clickHistory != null) {
            this.clickHistory.addAll(clickHistory);
        }
    }
    
    @Override
    public int[] state() {
        return Arrays.copyOf(state, state.length);
    }
    
    @Override
    public List<Point> clickHistory() {
        return new LinkedList<>(clickHistory);
    }
    
    public float[] build() {
        final float[] s
                = new float[state.length];
        for(int i = 0; i < s.length; i ++) {
            s[i] = state[i];
        }
        return s;
    }
    
    public float[] buildPath() {
        final float[] targets
                = new float[state.length];
        Arrays.fill(targets, OFF_VAL);
        for(final Point p : clickHistory) {
            targets[p.y*width+p.x] = ON_VAL;
        }
        return targets;
    }
    
    public State click(final Point p) {
        final List<Point> newHistory = addPoint(p);
        
        final int dex = p.y*width + p.x;
        final int up = p.y>0?(p.y-1)*width + p.x:-1;
        final int down = p.y<height-1?(p.y+1)*width + p.x:-1;
        final int left = p.x>0?p.y*width + (p.x-1):-1;
        final int right = p.x<width-1?p.y*width + (p.x+1):-1;
        
        final int[] newS = this.state();
        safeSwap(newS, dex, up, down, left, right);
        
        return new State(index + 1, height, width, newS, newHistory);
    }
    
    protected final List<Point> addPoint(final Point p) {
        final List<Point> newHistory = new LinkedList<>();
        newHistory.addAll(clickHistory);
        
        if(clickHistory.contains(p)) {
            newHistory.remove(p);
        } else {
            newHistory.add(p);
        }
        
        final List<Point> patternZero = new LinkedList(newHistory);
        final List<Point> patternOne = new LinkedList(newHistory);
        final List<Point> patternTwo = new LinkedList(newHistory);
        
        for(final Point test : PATTERNS[0]) {
            if(!patternZero.remove(test)) {
                patternZero.add(test);
            }
        }
        for(final Point test : PATTERNS[1]) {
            if(!patternOne.remove(test)) {
                patternOne.add(test);
            }
        }
        for(final Point test : PATTERNS[2]) {
            if(!patternTwo.remove(test)) {
                patternTwo.add(test);
            }
        }
        
        if(patternZero.size() < newHistory.size()) {
            newHistory.clear();
            newHistory.addAll(patternZero);
        }
        if(patternOne.size() < newHistory.size()) {
            newHistory.clear();
            newHistory.addAll(patternOne);
        }
        if(patternTwo.size() < newHistory.size()) {
            newHistory.clear();
            newHistory.addAll(patternTwo);
        }
        
        return newHistory;
    }
    
    public boolean isSolved() {
        boolean solved = true;
        for(int i : state) {
            solved = solved && (i != ON_VAL);
        }
        return solved;
    }
    
    public State print() {
        System.out.println("Board ::    " + index);
        System.out.println("SolutionDist :: " + clickHistory.size());
        System.out.println(this.toString());
        return this;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for(int y = 0; y < height; y ++) {
            final int[] stage = new int[width];
            for(int x = 0; x < width; x ++) {
                stage[x] = state[y*width+x];
            }
            builder.append(Arrays.toString(stage)).append("\n");
        }
        return builder.toString();
    }
    
    @Override
    public boolean equals(final Object o) {
        if(o instanceof State s) {
            boolean equals = true;
            for(int i = 0; i < s.state.length; i ++) {
                equals = equals && s.state[i] == state[i];
            }
            return equals;
        } return false;
    }
    
}
