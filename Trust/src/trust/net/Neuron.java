package trust.net;

import java.util.Random;
import jdk.incubator.vector.FloatVector;
import static jdk.incubator.vector.VectorOperators.ADD;
import jdk.incubator.vector.VectorSpecies;

/**
 *
 * @author MijitR <www.mijitr.xyz>
 */
public class Neuron {
    
    public static final float ETA = 0.001618f, MOMENTUM = 0.99f;
    
    private static final Random RAND;
    
    private static final VectorSpecies SPECIES = FloatVector.SPECIES_MAX;
    
    static {
        RAND = new Random();
    }
    
    final float[] weights, dwrtW, dwrtI, deltas, velocities;
    
    final int numInputs;
    
    private int mB;
    
    protected Neuron(final int numInputs) {
        weights = new float[numInputs + 1];
        for(int i = 0; i < numInputs; i ++) {
            weights[i] = (float)(RAND.nextGaussian()
                    * Math.sqrt(2f/numInputs));
        }
        dwrtI = new float[numInputs + 1];
        dwrtW = new float[numInputs + 1];
        deltas = new float[numInputs + 1];
        velocities = new float[numInputs + 1];
        this.numInputs = numInputs;
    }
    
    protected float process(final float[] input) {
        assert input.length == weights.length;
        System.arraycopy(input, 0, dwrtW, 0, input.length);
        System.arraycopy(weights, 0, dwrtI, 0, weights.length);
        return dot(input);
    }
    
    private float dotAVX(final float[] input) {
        FloatVector sum = FloatVector.zero(SPECIES);
        final int jumper = SPECIES.length();
        final int upperBound = SPECIES.loopBound(weights.length);
        for (int i = 0; i < upperBound; i += jumper) {
          var l = FloatVector.fromArray(SPECIES,weights, i);
          var r = FloatVector.fromArray(SPECIES,input, i);
          sum = l.fma(r, sum);
        }
        return sum.reduceLanes(ADD);
    }
    
    
    private float dot(final float[] input) {
        float sum = 0f;
        for(int i = 0; i < input.length; i ++) {
            sum += input[i] * weights[i];
        }
        return sum;
    }
    
    protected float[] spitErr(final float err) {
        final float[] influence
                = new float[numInputs];
        
        for(int i = 0; i < numInputs; i ++) {
            influence[i] = dwrtI[i] * err;
            deltas[i] += dwrtW[i] * err;
        }
        
        deltas[numInputs] += dwrtW[numInputs] * err;
        
        mB ++;
        
        return influence;
    }
    
    protected void update() {
        if(mB % Network.MINI_BATCH_SIZE == 0) {
            updateMomentumOP();
        }
    }
    
    private void updateSGD() {
        for(int w = 0; w < weights.length; w ++) {
            deltas[w] /= Math.max(1,mB);
            weights[w] -= ETA * deltas[w];
            deltas[w] = 0f;
        }
        mB = 0;
    }
    
    private void updateMomentumOP() {
        for(int w = 0; w < weights.length; w ++) {
            deltas[w] /= Math.max(1,mB);
            velocities[w] = MOMENTUM * velocities[w]
                    - (1f-MOMENTUM) * deltas[w];
            weights[w] += ETA * velocities[w];
            deltas[w] = 0f;
        }
        mB = 0;
    }
    
}
