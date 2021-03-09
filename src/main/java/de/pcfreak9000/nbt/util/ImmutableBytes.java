package de.pcfreak9000.nbt.util;

import java.util.Arrays;

public class ImmutableBytes extends AbstractImmutableArray<Byte> implements Iterable<Byte> {
    
    private final byte[] bytes;
    
    public ImmutableBytes(byte[] b) {
        this.bytes = b;
    }
    
    public byte[] arrayCopy() {
        return Arrays.copyOf(bytes, size());
    }
    
    public byte get(int index) {
        return bytes[index];
    }
    
    @Override
    public int size() {
        return bytes.length;
    }
    
    @Override
    protected Byte getBoxed(int index) {
        return get(index);
    }
    
    public byte[] arrayCopyOfRange(int offset, int length) {
        return Arrays.copyOfRange(bytes, offset, offset + length);
    }
    
}
