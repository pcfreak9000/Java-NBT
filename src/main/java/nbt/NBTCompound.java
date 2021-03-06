package nbt;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

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
        this.put(name, (NBTTag) compound);
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
    
    public NBTCompound getNode(String name) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTCompound) {
            return (NBTCompound) de;
        }
        throw new IllegalArgumentException("Entry doesnt exist or is not of the requested type");
    }
    
    public String getString(String name) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.StringEntry) {
            NBTTag.StringEntry se = (NBTTag.StringEntry) de;
            return se.getString();
        }
        throw new IllegalArgumentException("Entry doesnt exist or is not of the requested type");
    }
    
    public int getInt(String name) {
        checkNameValid(name);
        NBTTag de = entries.get(name);
        if (de instanceof NBTTag.IntEntry) {
            NBTTag.IntEntry ie = (NBTTag.IntEntry) de;
            return ie.getInt();
        }
        throw new IllegalArgumentException("Entry doesnt exist or is not of the requested type");
    }
    
    public Iterator<Map.Entry<String, NBTTag>> iterator() {
        return entriesImmutable.entrySet().iterator();
    }
    
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
