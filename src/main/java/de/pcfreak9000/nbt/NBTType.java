package de.pcfreak9000.nbt;

import java.util.NoSuchElementException;

public enum NBTType {
    End(0), Byte(1), Short(2), Int(3), Long(4), Float(5), Double(6), ByteArray(7), String(8), List(9), Compound(10),
    IntArray(11), LongArray(12);
    
    public final int id;
    
    private NBTType(int id) {
        this.id = id;
    }
    
    public static NBTType getById(int id) {
        if (id < 0 || id >= NBTType.values().length) {
            throw new NoSuchElementException("Type with id: " + id);
        }
        return NBTType.values()[id];
    }
}
