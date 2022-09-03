package de.pcfreak9000.nbt;

import de.pcfreak9000.nbt.visitor.NBTValueVisitor;

public class SmartIntEntry extends NBTTag {
    
    private final NBTList parent;
    private long l;
    
    public SmartIntEntry(NBTList parent, long l) {
        super(null);
        this.parent = parent;
        this.l = l;
    }
    
    public long getSmartInt() {
        return l;
    }
    
    @Override
    public void accept(NBTValueVisitor visitor) {
        switch (type()) {
        case Byte:
            visitor.visitByte((byte) l);
            break;
        case Short:
            visitor.visitShort((short) l);
            break;
        case Int:
            visitor.visitInt((int) l);
            break;
        default:
            visitor.visitLong(l);
            break;
        }
    }
    
    @Override
    public NBTType type() {
        return parent.getEntryType();
    }
    
    @Override
    public NBTTag cpy() {
        return new SmartIntEntry(parent, l);
    }
    
}
