package Model;

public class Space {

    private Integer actual;
    private final int expected;
    private final boolean fixed;
    private int current;

    public Space(int expected, boolean fixed) {
        this.expected = expected;
        this.fixed = fixed;
        this.current  = fixed ? expected : 0;
        
        if (fixed){
            actual = expected;
        }
    }

    public Integer getActual() {
        return actual;
    }

    public void setActual(final Integer actual) {
        if (fixed) return;
        this.actual = actual;
    }

    public void clearSpace(){
        setActual(null);
    }

    public int getExpected() {
        return expected;
    }

    public boolean isFixed(){
        return fixed;
    }

    public int getCurrent() {
         return current; 
    }

    public void setCurrent(int current) {
        if (!fixed) this.current = current;
    }
    
}
