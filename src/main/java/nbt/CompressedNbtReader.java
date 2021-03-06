package nbt;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import nbt.visitor.NBTValueVisitor;

public class CompressedNbtReader implements Closeable {
    
    private final NbtReader reader;
    
    public CompressedNbtReader(InputStream instream) throws IOException {
        this.reader = new NbtReader(new GZIPInputStream(instream));
    }
    
    public void applyVisitor(NBTValueVisitor visitor) throws IOException {
        this.reader.applyVisitor(visitor);
    }
    
    @Override
    public void close() throws IOException {
        this.reader.close();
    }
    
    public NBTTag toTag() throws IOException {
        return this.reader.toTag();
    }
    
    public NBTCompound toCompoundTag() throws IOException {
        return this.reader.toCompoundTag();
    }
    
    public String getName() {
        return this.reader.getName();
    }
}
