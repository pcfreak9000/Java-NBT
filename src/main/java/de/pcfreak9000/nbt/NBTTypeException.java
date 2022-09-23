package de.pcfreak9000.nbt;

public class NBTTypeException extends IllegalArgumentException {
    
    private static final long serialVersionUID = 7029670484599889922L;
    
    public NBTTypeException(String msg) {
        super(msg);
    }
}
