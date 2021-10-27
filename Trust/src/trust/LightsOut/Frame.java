/*
 * Dis how ballers do...
 */

package trust.LightsOut;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author MijitR
 */
public class Frame extends JFrame {
    
    public volatile boolean isWindowUp;

    private final Block[][] window;
    
    private final CustomListener listener;
    
    public Frame(final int width, final int height) {
        super("Lights Out");
        
        super.addWindowListener(new Minimizer());
        
        final Container c = super.getContentPane();
        
        c.setBackground(Color.BLUE.brighter());
        
        final LayoutManager layout = new GridLayout(width,height,2,2);
        c.setLayout(layout);
        
        listener = new CustomListener();
        
        window = new Block[height][width];
        for(int y = 0; y < height; y ++) {
            for(int x = 0; x < width; x ++) {
                window[y][x] = new Block(y*width + x,x,y);
                window[y][x].addMouseListener(listener);
                c.add(window[y][x]);
            }
        }
        
        super.pack();
        super.setVisible(true);
        
        isWindowUp = true;
    }
    
    public void click(final Point p) {
        window[p.y][p.x].value
                = window[p.y][p.x].value == State.OFF_VAL?
                    State.ON_VAL:State.OFF_VAL;
        if(p.x > 0)
            window[p.y][p.x-1].value
                    = window[p.y][p.x-1].value == State.OFF_VAL?
                        State.ON_VAL:State.OFF_VAL;
        if(p.x < window[0].length-1)
            window[p.y][p.x+1].value
                    = window[p.y][p.x+1].value == State.OFF_VAL?
                        State.ON_VAL:State.OFF_VAL;
        if(p.y > 0)
            window[p.y-1][p.x].value
                    = window[p.y-1][p.x].value == State.OFF_VAL?
                        State.ON_VAL:State.OFF_VAL;
        if(p.y < window.length-1)
            window[p.y+1][p.x].value
                    = window[p.y+1][p.x].value == State.OFF_VAL?
                        State.ON_VAL:State.OFF_VAL;
        super.repaint();
    }
    
    public void display(final State s) {
        final int[] stater = s.state();
        for(int y = 0, i = 0; y < window.length; y ++) {
            for(int x = 0; x < window[y].length; x ++, i ++) {
                window[y][x].setValue(stater[i]);
            }
        }
        super.repaint();
        
    }
 
    class Block extends JPanel {
        public static final Color[] colors
                = new Color[]{Color.BLACK, Color.ORANGE.darker()};
        final Point pos;
        final int index;
        private int value;
        Block(final int index, final int x, final int y) {
            this.pos = new Point(x,y);
            this.index = index;
            value = State.OFF_VAL;
        }
        @Override
        public final void paintComponent(final Graphics g) {
            super.paintComponent(g);
            final Graphics2D g2 = (Graphics2D)g.create();
            if(value == State.OFF_VAL) {
                g2.setColor(colors[0]);
            } else {
                g2.setColor(colors[1]);
            }
            g2.fillRect(0,0,super.getWidth(),super.getHeight());
            g2.dispose();
        }
        @Override
        public final Dimension getPreferredSize() {
            return new Dimension(75,75);
        }
        public final void setValue(final int val) {
            this.value = val;
        }
    }
    
    private class CustomListener implements MouseListener {

        @Override
        public void mouseClicked(final MouseEvent e) {
            if(e.getComponent() instanceof Block b) {
                Frame.this.click(b.pos);
            }
        }

        @Override
        public void mousePressed(final MouseEvent e) {
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
        }

        @Override
        public void mouseEntered(final MouseEvent e) {
        }

        @Override
        public void mouseExited(final MouseEvent e) {
        }
        
    }
    
    private class Minimizer implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {
            System.out.println("Hello");
        }

        @Override
        public void windowClosing(WindowEvent e) {
            Frame.super.dispose();
        }

        @Override
        public void windowClosed(WindowEvent e) {
            System.exit(0);
        }

        @Override
        public void windowIconified(WindowEvent e) {
            isWindowUp = false;
            System.out.println("Bye");
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
            isWindowUp = true;
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }
        
    }
    
}
