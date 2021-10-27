/*
 * Dis how ballers do...
 */

package trust.testing;

import java.awt.Point;
import java.util.Arrays;
import trust.LightsOut.State;

/**
 *
 * @author MijitR
 */
public class StateTest {
    public static final void main(final String[] args) {
        /*final int[] myState = new int[]{1,-1,-1,-1};
        
        final State s = new State(-1,2,2,new int[]{-1,-1,-1,1});
        final State s2 = new State(-1,2,2,myState);
        
        System.out.println("== ?");
        myState[2] += 4;
        System.out.println(Arrays.toString(myState));
        System.out.println(Arrays.toString(s2.state()));
        System.out.println("\\\\\\\\\\\\\\\\");
        
        
        System.out.println("== ?");
        final int[] myRef = s.state();
        myRef[1] += 4;
        System.out.println((Arrays.toString(myRef)));
        System.out.println(Arrays.toString(s.state()));*/
        
        final int[] startState
                = new int[]{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
                    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        
        final State s = new State(0,5,5,startState, null);
        
        s.print();
        
        s.click(new Point(3,2)).print();
        s.click(new Point(0,4)).print();
        final State sNext = s.click(new Point(4,4)).click(new Point(0,0)).print();

        for(final Point p : sNext.clickHistory()) {
            System.out.println(p);
        }
    }
}
