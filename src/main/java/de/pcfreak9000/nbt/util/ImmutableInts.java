package de.pcfreak9000.nbt.util;

import java.util.Arrays;

public class ImmutableInts extends AbstractImmutableArray<Integer> {
    
    private final int[] ints;
    
    public ImmutableInts(int[] ints) {
        this.ints = ints;
    }
    
    public int get(int index) {
        return ints[index];
    }
    
    public int[] arrayCopy() {
        return Arrays.copyOf(ints, size());
    }
    
    @Override
    public int size() {
        return ints.length;
    }
    
    @Override
    protected Integer getBoxed(int index) {
        return get(index);
    }
    
    public int[] arrayCopyOfRange(int offset, int length) {
        return Arrays.copyOfRange(ints, offset, offset + length);
    }
    
}
