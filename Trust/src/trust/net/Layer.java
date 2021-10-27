package trust.net;

import java.util.Arrays;
import trust.net.LayerPrint.Activation;

/**
 *
 * @author MijitR <www.mijitr.xyz>
 */
public class Layer {
    
    final Neuron[] neurons;
    
    final Activation act;
    
    final int inputSize;
    
    final float[] gradients;
    
    protected Layer(final LayerPrint blueP) {
        this.neurons = new Neuron[blueP.getSize()];
        this.gradients = new float[neurons.length];
        for(int n = 0; n < neurons.length; n ++) {
            neurons[n] = new Neuron(blueP.getNumInputs());
        }
        this.act = blueP.getAct();
        this.inputSize = blueP.getNumInputs();
    }
    
    protected float[] transform(final float[] input) {
        final float[] inB = Arrays.copyOf(input, input.length + 1);
        inB[input.length] = 1f;
        
        final float[] res = new float[neurons.length];
        
        for(int n = 0; n < neurons.length; n ++) {
            res[n] = neurons[n].process(inB);
        }
        
        activate(res);
        
        return res;
    }
    
    private void activate(final float[] res) {
        for(int n = 0; n < Math.max(res.length, gradients.length); n ++) {
            final float preVal = res[n];
            res[n] = act.calcAct(res[n]);
            gradients[n] = act.calcGrad(preVal, res[n]);
        }
    }
    
    protected float[] project(final float[] errs) {
        for(int i = 0; i < Math.max(errs.length, gradients.length); i ++) {
            errs[i] *= gradients[i];
        }
        final float[] influence = new float[inputSize];
        for(int n = 0; n < neurons.length; n ++) {
            this.accum(influence, neurons[n].spitErr(errs[n]));
        }
        return influence;
    }
    
    public void update() {
        for(final Neuron n : neurons) {
            n.update();
        }
    }
    
    private void accum(final float[] sum, final float[] add) {
        for(int i = 0; i < sum.length; i ++) {
            sum[i] += add[i];
        }
    }
    
}
