package nbt;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import nbt.visitor.AbstractNBTValueVisitor;

public class CompressedNbtWriter extends AbstractNBTValueVisitor implements Flushable, Closeable {
    
    public CompressedNbtWriter(OutputStream out) throws IOException {
        super(new NbtWriter(new GZIPOutputStream(out)));
    }
    
    @Override
    public void close() throws IOException {
        ((NbtWriter) this.parentVisitor).close();
    }
    
    @Override
    public void flush() throws IOException {
        ((NbtWriter) this.parentVisitor).flush();
    }
}
