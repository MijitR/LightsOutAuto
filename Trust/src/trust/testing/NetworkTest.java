package trust.testing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import trust.net.LayerPrint;
import trust.net.Network;

/**
 *
 * @author MijitR <www.mijitr.xyz>
 */
public class NetworkTest {
    
    public static final void main(final String[] args) {
         
        final List<LayerPrint> design
               = new LinkedList<>();
        
        /*design.add(
            new LayerPrint()
                    .setSize(2)
                    .setNumInputs(2)
                    .setActivation(LayerPrint.Activation.TANH)
        );*/
        
        design.add(
            new LayerPrint()
                    .setSize(8)
                    .setNumInputs(2)
                    .setActivation(LayerPrint.Activation.TANH)
        );
        
        design.add(
            new LayerPrint(1)
                    .setNumInputs(8)
                    .setActivation(LayerPrint.Activation.TANH)
        );
        
        final Network net
                = new Network(design);
        
        
        XOrGate(net);
        
    }
    
    private static void XOrGate(final Network net) {
        final float[] input1 = new float[]{1,1};
        final float[] input2 = new float[]{-1,1};//,-1,1,-1,1,-1,1,-1,1,1,1,1,1,1,1};
        final float[] input3 = new float[]{1,-1};
        final float[] input4 = new float[]{-1,-1};
        
        final float[] target1 = new float[]{-1};//,1,-1,1,-1,1,-1,1,-1,1,-1,1,-1,1,-1,1};
        final float[] target2 = new float[]{1};//,-1,1,-1,1,-1,1,-1,1,-1,1,-1,1,-1,1,-1};
        final float[] target3 = new float[]{1};
        final float[] target4 = new float[]{-1};
        
        for(int epoc = 0; epoc < 15500; epoc ++) {
            final float[] res = net.operate(input1);
            net.train(target1);
            final float[] res2 = net.operate(input2);
            net.train(target2);
            final float[] res3 = net.operate(input3);
            net.train(target3);
            final float[] res4 = net.operate(input4);
            net.train(target4);
            if(epoc % 20 == 0) {
                System.out.println("Epoc : " + epoc);
                System.out.println(net.err());
                System.out.println(Arrays.toString(res));
                System.out.println(Arrays.toString(res2));
                System.out.println(Arrays.toString(res3));
                System.out.println(Arrays.toString(res4));
            }
        }
        
        System.out.println("//////////////////////");
        System.out.println(Arrays.toString(net.operate(input1)));
        System.out.println(Arrays.toString(net.operate(input2)));
        
        System.out.println(Arrays.toString(net.operate(input3)));
        System.out.println(Arrays.toString(net.operate(input4)));
    }
    
}
