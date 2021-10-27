package trust.net;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author MijitR <www.mijitr.xyz>
 */
public class Network {
    
    public static final int MINI_BATCH_SIZE = 3;
    
    private final Layer[] layers;
    
    private float[] lastResults;
    
    private float err;
    
    public Network(final List<LayerPrint> design) {
        layers = new Layer[design.size()];
        for(int i = 0; i < layers.length; i ++) {
            layers[i] = new Layer(design.get(i));
        }
    }
    
    public final float[] operate(final float[] inputs) {
        float[] cp = Arrays.copyOf(inputs, inputs.length);
        for(final Layer l : layers) {
            cp = l.transform(cp);
        }
        lastResults = Arrays.copyOf(cp, cp.length);
        return cp;
    }
    
    public final float[] train(final float[] targets) {
        assert targets.length == lastResults.length;
        
        float[] bcp = Arrays.copyOf(targets, targets.length);
        err = 0f;
        
        for(int i = 0; i < targets.length; i ++) {
            bcp[i] = lastResults[i] - bcp[i];
            err += 0.5f * bcp[i]*bcp[i];
        }
        
        err /= targets.length;
        
        for(int lay = layers.length-1; lay >= 0; lay --) {
            bcp = layers[lay].project(bcp);
        }
        
        for(final Layer lay : layers) {
            lay.update();
        }
        
        return bcp;
    }
    
    public final float err() {
        return err;
    }
    
}
