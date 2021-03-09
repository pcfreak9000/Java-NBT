package de.pcfreak9000.nbt.util;

import java.util.Iterator;
import java.util.StringJoiner;

abstract class AbstractImmutableArray<T> implements Iterable<T> {
    
    @Override
    public String toString() {
        return toString("[", ",", "]");
    }
    
    public String toString(String prefix, String delimiter, String suffix) {
        StringJoiner joiner = new StringJoiner(delimiter, prefix, suffix);
        for (int i = 0; i < size(); i++) {
            joiner.add(getBoxed(i).toString());
        }
        return joiner.toString();
    }
    
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            
            private int index = 0;
            
            @Override
            public boolean hasNext() {
                return index < AbstractImmutableArray.this.size();
            }
            
            @Override
            public T next() {
                return AbstractImmutableArray.this.getBoxed(index++);
            }
        };
    }
    
    public abstract int size();
    
    protected abstract T getBoxed(int index);
}
