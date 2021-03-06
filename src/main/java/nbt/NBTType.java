package nbt;

import java.util.Arrays;

public enum NBTType {
    End(0), Byte(1), Short(2), Int(3), Long(4), Float(5), Double(6), ByteArray(7), String(8), List(9), Compound(10);
    
    public final int id;
    
    private NBTType(int id) {
        this.id = id;
    }
    
    public static NBTType getById(int id) {
        //TODO replace with faster variant
        return Arrays.stream(NBTType.values()).filter((t) -> t.id == id).findFirst().get();
    }
}
