package de.pcfreak9000.nbt;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

public class NBTCompound extends NBTTag {
    
    private static final String NAME_REGEX = "^[\\w\\d][\\w\\d ]*$";
    
    private Map<String, NBTTag> entries;
    private Map<String, NBTTag> entriesImmutable;
    
    public NBTCompound() {
        super(NBTType.Compound);
        this.entries = new LinkedHashMap<>();
        this.entriesImmutable = Collections.unmodifiableMap(this.entries);
    }
    
    @Override
    public NBTCompound cpy() {
        NBTCompound c = new NBTCompound();
        for (Map.Entry<String, NBTTag> e : this.entries.entrySet()) {
            c.put(e.getKey(), e.getValue().cpy());
        }
        return c;
    }
    
    public boolean isEmpty() {
        return entries.isEmpty();
    }
    
    public boolean hasKey(String name) {
        return entries.containsKey(name);
    }
    
    public NBTTag remove(String name) {
        return entries.remove(name);
    }
    
    public NBTTag remove(String name, NBTType type) {
        NBTTag tag = entries.get(name);
        if (tag != null && tag.type() == type) {
            return entries.remove(name);
        }
        return null;
    }
    
    public void removeAll() {
        entries.clear();
    }
    
    public void putCompound(String name, NBTCompound compound) {
        this.put(name, compound);
    }
    
    public void putList(String name, NBTList list) {
        this.put(name, list);
    }
    
    public void put(String name, NBTTag entry) {
        checkNameValid(name);
        Objects.requireNonNull(entry);
        entries.put(name, entry);
    }
    
    public void putByte(String name, byte i) {
        checkNameValid(name);
        entries.put(name, new NBTTag.ByteEntry(i));
    }
    
    public void putShort(String name, short i) {
        checkNameValid(name);
        entries.put(name, new NBTTag.ShortEntry(i));
    }
    
    public void putInt(String name, int i) {
        checkNameValid(name);
        entries.put(name, new NBTTag.IntEntry(i));
    }
    
    public void putLong(String name, long i) {
        checkNameValid(name);
        entries.put(name, new NBTTag.LongEntry(i));
    }
    
    public void putFloat(String name, float i) {
        checkNameValid(name);
        entries.put(name, new NBTTag.FloatEntry(i));
    }
    
    public void putDouble(String name, double i) {
        checkNameValid(name);
        entries.put(name, new NBTTag.DoubleEntry(i));
    }
    
    public void putString(String name, String string) {
        checkNameValid(name);
        Objects.requireNonNull(string);
        entries.put(name, new NBTTag.StringEntry(string));
    }
    
    public void putByteArray(String name, byte[] immutableBytes) {
        checkNameValid(name);
        Objects.requireNonNull(immutableBytes);
        entries.put(name, new ByteArrayEntry(immutableBytes));
    }
    
    public void putIntArray(String name, int[] immutableints) {
        checkNameValid(name);
        Objects.requireNonNull(immutableints);
        entries.put(name, new IntArrayEntry(immutableints));
    }
    
    public void putLongArray(String name, long[] immutablelongs) {
        checkNameValid(name);
        Objects.requireNonNull(immutablelongs);
        entries.put(name, new LongArrayEntry(immutablelongs));
    }
    
    public void putIntegerSmart(String name, long value) {
        if (value == (byte) value) {
            putByte(name, (byte) value);
        } else if (value == (short) value) {
            putShort(name, (short) value);
        } else if (value == (int) value) {
            putInt(name, (int) value);
        } else {
            putLong(name, value);
        }
    }
    
    public long getIntegerSmart(String name) {
        NBTTag t = get(name);
        if (t instanceof NBTTag.ByteEntry) {
            return getByte(name);
        } else if (t instanceof NBTTag.ShortEntry) {
            return getShort(name);
        } else if (t instanceof NBTTag.IntEntry) {
            return getInt(name);
        } else {
            return getLong(name);
        }
    }
    
    public long getIntegerSmartOrDefault(String name, long def) {
        checkNameValid(name);
        NBTTag t = entries.get(name);
        if (t == null) {
            return def;
        }
        if (t instanceof NBTTag.ByteEntry) {
            return getByte(name);
        } else if (t instanceof NBTTag.ShortEntry) {
            return getShort(name);
        } else if (t instanceof NBTTag.IntEntry) {
            return getInt(name);
        } else {
            return getLong(name);
        }
    }
    
    public void putBooleanAsByte(String name, boolean b) {
        putByte(name, b ? (byte) 1 : 0);
    }
    
    public NBTTag get(String name) {
        checkNameValid(name);
        NBTTag tag = entries.get(name);
        if (tag != null) {
            return tag;
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public NBTTag getOrDefault(String name, NBTTag def) {
        checkNameValid(name);
        NBTTag tag = entries.get(name);
        if (tag != null) {
            return tag;
        }
        return def;
    }
    
    public NBTCompound getCompound(String name) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTCompound) {
            return (NBTCompound) de;
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public NBTCompound getCompoundOrDefault(String name, NBTCompound def) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTCompound) {
            return (NBTCompound) de;
        }
        if (de == null) {
            return def;
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public NBTList getList(String name) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTList) {
            return (NBTList) de;
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public NBTList getListOrDefault(String name, NBTList def) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTList) {
            return (NBTList) de;
        }
        if (de == null) {
            return def;
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public String getString(String name) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.StringEntry) {
            NBTTag.StringEntry se = (NBTTag.StringEntry) de;
            return se.getString();
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public String getStringOrDefault(String name, String def) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.StringEntry) {
            NBTTag.StringEntry se = (NBTTag.StringEntry) de;
            return se.getString();
        }
        if (de == null) {
            return def;
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public byte getByte(String name) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.ByteEntry) {
            NBTTag.ByteEntry ie = (NBTTag.ByteEntry) de;
            return ie.getByte();
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public boolean getBooleanFromByte(String name) {
        return getByte(name) != 0;
    }
    
    public boolean getBooleanFromByteOrDefault(String name, boolean b) {
        return getByteOrDefault(name, b ? (byte) 1 : 0) != 0;
    }
    
    public byte getByteOrDefault(String name, byte def) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.ByteEntry) {
            NBTTag.ByteEntry ie = (NBTTag.ByteEntry) de;
            return ie.getByte();
        }
        if (de == null) {
            return def;
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public short getShort(String name) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.ShortEntry) {
            NBTTag.ShortEntry ie = (NBTTag.ShortEntry) de;
            return ie.getShort();
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public short getShortOrDefault(String name, short def) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.ShortEntry) {
            NBTTag.ShortEntry ie = (NBTTag.ShortEntry) de;
            return ie.getShort();
        }
        if (de == null) {
            return def;
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public int getInt(String name) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.IntEntry) {
            NBTTag.IntEntry ie = (NBTTag.IntEntry) de;
            return ie.getInt();
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public int getIntOrDefault(String name, int def) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.IntEntry) {
            NBTTag.IntEntry ie = (NBTTag.IntEntry) de;
            return ie.getInt();
        }
        if (de == null) {
            return def;
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public long getLong(String name) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.LongEntry) {
            NBTTag.LongEntry le = (NBTTag.LongEntry) de;
            return le.getLong();
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public long getLongOrDefault(String name, long def) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.LongEntry) {
            NBTTag.LongEntry le = (NBTTag.LongEntry) de;
            return le.getLong();
        }
        if (de == null) {
            return def;
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
        
    }
    
    public float getFloat(String name) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.FloatEntry) {
            NBTTag.FloatEntry ie = (NBTTag.FloatEntry) de;
            return ie.getFloat();
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public float getFloatOrDefault(String name, float def) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.FloatEntry) {
            NBTTag.FloatEntry ie = (NBTTag.FloatEntry) de;
            return ie.getFloat();
        }
        if (de == null) {
            return def;
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public double getDouble(String name) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.DoubleEntry) {
            NBTTag.DoubleEntry ie = (NBTTag.DoubleEntry) de;
            return ie.getDouble();
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public double getDoubleOrDefault(String name, double def) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.DoubleEntry) {
            NBTTag.DoubleEntry ie = (NBTTag.DoubleEntry) de;
            return ie.getDouble();
        }
        if (de == null) {
            return def;
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public byte[] getByteArray(String string) {
        checkNameValid(string);
        NBTTag t = entries.get(string);
        if (t instanceof ByteArrayEntry) {
            ByteArrayEntry bae = (ByteArrayEntry) t;
            return bae.getBytes();
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public int[] getIntArray(String string) {
        checkNameValid(string);
        NBTTag t = entries.get(string);
        if (t instanceof IntArrayEntry) {
            IntArrayEntry bae = (IntArrayEntry) t;
            return bae.getInts();
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public long[] getLongArray(String string) {
        checkNameValid(string);
        NBTTag t = entries.get(string);
        if (t instanceof LongArrayEntry) {
            LongArrayEntry bae = (LongArrayEntry) t;
            return bae.getLongs();
        }
        throw new NBTTypeException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public Set<Entry<String, NBTTag>> entrySet() {
        return entriesImmutable.entrySet();
    }
    
    public Set<String> keySet() {
        return entriesImmutable.keySet();
    }
    
    private static final String EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT = "Entry does not exist or is not of the requested type";
    
    private void checkNameValid(String name) {
        Objects.requireNonNull(name);
        if (name.isEmpty() || name.isBlank()) {
            throw new IllegalArgumentException("Name is empty or blank");
        }
        if (!name.matches(NAME_REGEX)) {
            throw new IllegalArgumentException("Name containes illegal characters: " + name);
        }
    }
    
}
