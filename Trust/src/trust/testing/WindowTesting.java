/*
 * Dis how ballers do...
 */

package trust.testing;

import javax.swing.SwingUtilities;
import trust.LightsOut.Frame;

/**
 *
 * @author MijitR
 */
public class WindowTesting {
    
    private static Frame frame;
    
    public static final void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new Frame(5,5);
        });
        
        
    }
}
