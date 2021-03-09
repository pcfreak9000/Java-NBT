package de.pcfreak9000.nbt.util;

import java.util.Arrays;

public class ImmutableLongs extends AbstractImmutableArray<Long> {
    
    private final long[] longs;
    
    public ImmutableLongs(long[] longs) {
        this.longs = longs;
    }
    
    public long get(int index) {
        return longs[index];
    }
    
    public long[] arrayCopy() {
        return Arrays.copyOf(longs, size());
    }
    
    @Override
    public int size() {
        return longs.length;
    }
    
    @Override
    protected Long getBoxed(int index) {
        return get(index);
    }
    
    public long[] arrayCopyOfRange(int offset, int length) {
        return Arrays.copyOfRange(longs, offset, offset + length);
    }
}
