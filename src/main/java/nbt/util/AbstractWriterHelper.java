package nbt.util;

import java.io.IOException;
import java.util.List;

public abstract class AbstractWriterHelper<T> {
    
    public static interface SupressedRunneable {
        void run() throws IOException;
    }
    
    protected final T dataOut;
    
    protected final List<IOException> supressedExceptions;
    
    public AbstractWriterHelper(T s, List<IOException> es) {
        this.dataOut = s;
        this.supressedExceptions = es;
    }
    
    protected void run(SupressedRunneable r) {
        if (supressedExceptions.isEmpty()) {
            try {
                r.run();
            } catch (IOException e) {
                supressedExceptions.add(e);
            }
        }
    }
}
