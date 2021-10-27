package trust.net;

/**
 *
 * @author MijitR <www.mijitr.xyz>
 */
public class LayerPrint {
    
    public enum Activation {
        IDENTITY ((x)->x,(x,y)->1),
        TANH ((x)->(float)Math.tanh(x),(x,y)->1f-y*y),
        SIGMOID ((x)->(float)(1d/(1d + Math.exp(-x))), (x,y)->y*(1f-y)),
        RELU ((x)->Math.max(0f,x),(x,y)->x>=0f?1f:0f),
        SIN ((x)->(float)Math.sin(x),(x,y)->(float)Math.cos(x));
        
        private final FloatUnaryOperator act;
        private final FloatBinaryOperator grad;
        
        private Activation(final FloatUnaryOperator act,
                final FloatBinaryOperator grad) {
            this.act = act;
            this.grad = grad;
        }
        
        public final float calcAct(final float input) {
            return act.applyAsFloat(input);
        }
        public final float calcGrad(final float preVal, final float acted) {
            return grad.applyAsFloat(preVal, acted);
        }
    }
    
    private int size, inputCount;
    
    private Activation act;
    
    public LayerPrint() {
        
    }
    
    public LayerPrint(final int size) {
        this.size = size;
    }
    
    public LayerPrint setActivation(final Activation act) {
        this.act = act;
        return this;
    }
    
    public LayerPrint setSize(final int size) {
        this.size = size;
        return this;
    }
    
    public LayerPrint setNumInputs(final int numInputs) {
        this.inputCount = numInputs;
        return this;
    }
    
    public Activation getAct() {
        return act;
    }
    
    public int getSize() {
        return size;
    }
    
    public int getNumInputs() {
        return inputCount;
    }
    
    @FunctionalInterface
    public interface FloatUnaryOperator {
        float applyAsFloat(float in);
    }
    
    @FunctionalInterface
    public interface FloatBinaryOperator {
        float applyAsFloat(float a, float b);
    }
    
}
