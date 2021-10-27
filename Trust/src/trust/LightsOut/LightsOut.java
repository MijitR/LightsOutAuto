/*
 * Dis how ballers do...
 */

package trust.LightsOut;

import javax.swing.SwingUtilities;

/**
 *
 * @author MijitR
 */
public class LightsOut {

    public static final void main(final String[] args) {
        SwingUtilities.invokeLater(
             () -> new Frame(5,5)
        );
    }
    
}
