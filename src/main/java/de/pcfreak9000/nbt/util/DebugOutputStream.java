package de.pcfreak9000.nbt.util;

import java.io.IOException;
import java.io.OutputStream;

public class DebugOutputStream extends OutputStream {
    
    @Override
    public void write(int b) throws IOException {
        System.out.println("Writing byte: " + b);
        if (b == 77) {
            System.err.println("77");
          //  throw new RuntimeException("77");
        }
    }
    
}
