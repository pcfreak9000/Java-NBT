package de.pcfreak9000.nbt;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import de.pcfreak9000.nbt.util.ImmutableBytes;
import de.pcfreak9000.nbt.util.ImmutableInts;
import de.pcfreak9000.nbt.util.ImmutableLongs;

public class NBTCompound extends NBTTag {
    
    private static final String NAME_REGEX = "^[\\w\\d][\\w\\d ]*$";
    
    private Map<String, NBTTag> entries;
    private Map<String, NBTTag> entriesImmutable;
    
    public NBTCompound() {
        super(NBTType.Compound);
        this.entries = new LinkedHashMap<>();
        this.entriesImmutable = Collections.unmodifiableMap(this.entries);
    }
    
    public boolean hasKey(String name) {
        return entries.containsKey(name);
    }
    
    public void remove(String name) {
        entries.remove(name);
    }
    
    public void putCompound(String name, NBTCompound compound) {
        this.put(name, compound);
    }
    
    public void putList(String name, NBTList list) {
        this.put(name, list);
    }
    
    void put(String name, NBTTag entry) {
        checkNameValid(name);
        checkNameExists(name);
        Objects.requireNonNull(entry);
        entries.put(name, entry);
    }
    
    public void putByte(String name, byte i) {
        checkNameValid(name);
        checkNameExists(name);
        entries.put(name, new NBTTag.ByteEntry(i));
    }
    
    public void putShort(String name, short i) {
        checkNameValid(name);
        checkNameExists(name);
        entries.put(name, new NBTTag.ShortEntry(i));
    }
    
    public void putInt(String name, int i) {
        checkNameValid(name);
        checkNameExists(name);
        entries.put(name, new NBTTag.IntEntry(i));
    }
    
    public void putLong(String name, long i) {
        checkNameValid(name);
        checkNameExists(name);
        entries.put(name, new NBTTag.LongEntry(i));
    }
    
    public void putFloat(String name, float i) {
        checkNameValid(name);
        checkNameExists(name);
        entries.put(name, new NBTTag.FloatEntry(i));
    }
    
    public void putDouble(String name, double i) {
        checkNameValid(name);
        checkNameExists(name);
        entries.put(name, new NBTTag.DoubleEntry(i));
    }
    
    public void putString(String name, String string) {
        checkNameValid(name);
        checkNameExists(name);
        Objects.requireNonNull(string);
        entries.put(name, new NBTTag.StringEntry(string));
    }
    
    public void putByteArray(String name, ImmutableBytes immutableBytes) {
        checkNameValid(name);
        checkNameExists(name);
        Objects.requireNonNull(immutableBytes);
        entries.put(name, new ByteArrayEntry(immutableBytes));
    }
    
    public void putIntArray(String name, ImmutableInts immutableints) {
        checkNameValid(name);
        checkNameExists(name);
        Objects.requireNonNull(immutableints);
        entries.put(name, new IntArrayEntry(immutableints));
    }
    
    public void putLongArray(String name, ImmutableLongs immutablelongs) {
        checkNameValid(name);
        checkNameExists(name);
        Objects.requireNonNull(immutablelongs);
        entries.put(name, new LongArrayEntry(immutablelongs));
    }
    
    public NBTCompound getNode(String name) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTCompound) {
            return (NBTCompound) de;
        }
        throw new IllegalArgumentException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public NBTCompound getNodeOrDefault(String name, NBTCompound def) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTCompound) {
            return (NBTCompound) de;
        }
        if (de == null) {
            return def;
        }
        throw new IllegalArgumentException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public String getString(String name) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.StringEntry) {
            NBTTag.StringEntry se = (NBTTag.StringEntry) de;
            return se.getString();
        }
        throw new IllegalArgumentException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
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
        throw new IllegalArgumentException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public byte getByte(String name) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.ByteEntry) {
            NBTTag.ByteEntry ie = (NBTTag.ByteEntry) de;
            return ie.getByte();
        }
        throw new IllegalArgumentException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
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
        throw new IllegalArgumentException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public int getInt(String name) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.IntEntry) {
            NBTTag.IntEntry ie = (NBTTag.IntEntry) de;
            return ie.getInt();
        }
        throw new IllegalArgumentException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
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
        throw new IllegalArgumentException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public long getLong(String name) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.LongEntry) {
            NBTTag.LongEntry le = (NBTTag.LongEntry) de;
            return le.getLong();
        }
        throw new IllegalArgumentException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
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
        throw new IllegalArgumentException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
        
    }
    
    public ImmutableBytes getByteArray(String string) {
        checkNameValid(string);
        NBTTag t = entries.get(string);
        if (t instanceof ByteArrayEntry) {
            ByteArrayEntry bae = (ByteArrayEntry) t;
            return bae.getBytes();
        }
        throw new IllegalArgumentException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public ImmutableInts getIntArray(String string) {
        checkNameValid(string);
        NBTTag t = entries.get(string);
        if (t instanceof IntArrayEntry) {
            IntArrayEntry bae = (IntArrayEntry) t;
            return bae.getInts();
        }
        throw new IllegalArgumentException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public ImmutableLongs getLongArray(String string) {
        checkNameValid(string);
        NBTTag t = entries.get(string);
        if (t instanceof LongArrayEntry) {
            LongArrayEntry bae = (LongArrayEntry) t;
            return bae.getLongs();
        }
        throw new IllegalArgumentException(EXCEPTION_NOTEXIST_INCORRECTTYPE_TEXT);
    }
    
    public Iterator<Map.Entry<String, NBTTag>> iterator() {
        return entriesImmutable.entrySet().iterator();
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
    
    private void checkNameExists(String name) {
        if (entries.containsKey(name)) {
            throw new IllegalStateException("Name is already in use: " + name);
        }
    }
    
}