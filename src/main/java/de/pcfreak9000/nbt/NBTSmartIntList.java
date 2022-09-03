package de.pcfreak9000.nbt;

import java.util.Objects;

public class NBTSmartIntList extends NBTList {
    
    public NBTSmartIntList() {
        this.entrytype = NBTType.Byte;
    }
    
    @Override
    protected void check(NBTType t) {
        throw new IllegalArgumentException();
    }
    
    public long getSmartInt(int index) {
        return ((SmartIntEntry) get(index)).getSmartInt();
    }
    
    @Override
    public long getNumberAutocast(int index) {
        return getSmartInt(index);
    }
    
    private void accomodateFor(long l) {
        switch (entrytype) {
        case Byte:
            if (l > Byte.MAX_VALUE || l < Byte.MIN_VALUE) {
                this.entrytype = NBTType.Short;
            }
        case Short:
            if (l > Short.MAX_VALUE || l < Short.MIN_VALUE) {
                this.entrytype = NBTType.Int;
            }
        case Int:
            if (l > Integer.MAX_VALUE || l < Integer.MIN_VALUE) {
                this.entrytype = NBTType.Long;
            }
        default:
            break;
        }
    }
    
    @Override
    public NBTList cpy() {
        NBTSmartIntList l = new NBTSmartIntList();
        for (NBTTag t : tags) {
            SmartIntEntry sie = (SmartIntEntry) t;
            l.addSmartInt(sie.getSmartInt());
        }
        return l;
    }
    
    private void addUnchecked(NBTTag tag) {
        Objects.requireNonNull(tag);
        tags.add(tag);
    }
    
    public void addSmartInt(long l) {
        accomodateFor(l);
        this.addUnchecked(new SmartIntEntry(this, l));
    }
    
}
