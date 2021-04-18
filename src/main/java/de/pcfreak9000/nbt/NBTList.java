package de.pcfreak9000.nbt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NBTList extends NBTTag {
    
    private final List<NBTTag> tags;
    private final List<NBTTag> tagsImmutable;
    private final NBTType entrytype;
    
    public NBTList(NBTType type) {
        super(NBTType.List);
        if (type == NBTType.End) {
            throw new IllegalArgumentException("Lists of End tags not allowed");
        }
        this.tags = new ArrayList<>();
        this.tagsImmutable = Collections.unmodifiableList(this.tags);
        this.entrytype = type;
    }
    
    @Override
    public NBTList cpy() {
        NBTList l = new NBTList(entrytype);
        for (NBTTag t : tags) {
            l.add(t.cpy());
        }
        return l;
    }
    
    public NBTType getEntryType() {
        return entrytype;
    }
    
    public int size() {
        return tags.size();
    }
    
    public List<NBTTag> getContent() {
        return tagsImmutable;
    }
    
    public boolean isEmpty() {
        return tags.isEmpty();
    }
    
    public void addByte(byte b) {
        check(NBTType.Byte);
        tags.add(new ByteEntry(b));
    }
    
    public void addShort(short b) {
        check(NBTType.Short);
        tags.add(new ShortEntry(b));
    }
    
    public void addInt(int b) {
        check(NBTType.Int);
        tags.add(new IntEntry(b));
    }
    
    public void addLong(long b) {
        check(NBTType.Long);
        tags.add(new LongEntry(b));
    }
    
    public void addFloat(float b) {
        check(NBTType.Float);
        tags.add(new FloatEntry(b));
    }
    
    public void addDouble(double b) {
        check(NBTType.Double);
        tags.add(new DoubleEntry(b));
    }
    
    public void addCompound(NBTCompound comp) {
        check(NBTType.Compound);
        Objects.requireNonNull(comp);
        tags.add(comp);
    }
    
    public void addList(NBTList list) {
        check(NBTType.List);
        Objects.requireNonNull(list);
        tags.add(list);
    }
    
    public void addString(String s) {
        check(NBTType.String);
        Objects.requireNonNull(s);
        tags.add(new StringEntry(s));
    }
    
    public void add(NBTTag tag) {
        Objects.requireNonNull(tag);
        check(tag.type());
        tags.add(tag);
    }
    
    public NBTTag get(int index) {
        return tags.get(index);
    }
    
    public String getString(int index) {
        check(NBTType.String);
        return ((NBTTag.StringEntry) get(index)).getString();
    }
    
    public byte getByte(int index) {
        check(NBTType.Byte);
        return ((NBTTag.ByteEntry) get(index)).getByte();
    }
    
    public short getShort(int index) {
        check(NBTType.Short);
        return ((NBTTag.ShortEntry) get(index)).getShort();
    }
    
    public int getInt(int index) {
        check(NBTType.Int);
        return ((NBTTag.IntEntry) get(index)).getInt();
    }
    
    public long getLong(int index) {
        check(NBTType.Long);
        return ((NBTTag.LongEntry) get(index)).getLong();
    }
    
    public float getFloat(int index) {
        check(NBTType.Float);
        return ((NBTTag.FloatEntry) get(index)).getFloat();
    }
    
    public double getDouble(int index) {
        check(NBTType.Double);
        return ((NBTTag.DoubleEntry) get(index)).getDouble();
    }
    
    public NBTCompound getCompound(int index) {
        check(NBTType.Compound);
        return (NBTCompound) get(index);
    }
    
    public NBTList getList(int index) {
        check(NBTType.List);
        return (NBTList) get(index);
    }
    
    private void check(NBTType t) {
        if (t != this.entrytype) {
            throw new IllegalArgumentException("Illegal type: " + t);
        }
    }
}
