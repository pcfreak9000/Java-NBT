package de.pcfreak9000.nbt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NBTList extends NBTTag {
    
    private final List<NBTTag> tags;
    private final List<NBTTag> unmodTags;
    private final NBTType entrytype;
    
    public NBTList(NBTType type) {
        super(NBTType.List);
        if (type == NBTType.End) {
            throw new IllegalArgumentException("Lists of End tags not allowed");
        }
        this.tags = new ArrayList<>();
        this.unmodTags = Collections.unmodifiableList(this.tags);
        this.entrytype = type;
    }
    
    public NBTType getEntryType() {
        return entrytype;
    }
    
    public List<NBTTag> getContent() {
        return unmodTags;
    }
    
    public boolean isEmpty() {
        return tags.isEmpty();
    }
    
    public void add(byte b) {
        check(NBTType.Byte);
        tags.add(new ByteEntry(b));
    }
    
    public void add(short b) {
        check(NBTType.Short);
        tags.add(new ShortEntry(b));
    }
    
    public void add(int b) {
        check(NBTType.Int);
        tags.add(new IntEntry(b));
    }
    
    public void add(long b) {
        check(NBTType.Long);
        tags.add(new LongEntry(b));
    }
    
    public void add(float b) {
        check(NBTType.Float);
        tags.add(new FloatEntry(b));
    }
    
    public void add(double b) {
        check(NBTType.Double);
        tags.add(new DoubleEntry(b));
    }
    
    public void add(NBTCompound comp) {
        check(NBTType.Compound);
        Objects.requireNonNull(comp);
        tags.add(comp);
    }
    
    public void add(NBTList list) {
        check(NBTType.List);
        Objects.requireNonNull(list);
        tags.add(list);
    }
    
    public void add(String s) {
        check(NBTType.String);
        Objects.requireNonNull(s);
        tags.add(new StringEntry(s));
    }
    
    public void add(NBTTag tag) {
        Objects.requireNonNull(tag);
        check(tag.type());
        tags.add(tag);
    }
    
    private void check(NBTType t) {
        if (t != this.entrytype) {
            throw new IllegalArgumentException("Illegal type: " + t);
        }
    }
}
