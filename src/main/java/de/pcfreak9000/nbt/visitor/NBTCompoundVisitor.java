package de.pcfreak9000.nbt.visitor;

public interface NBTCompoundVisitor {
    
    NBTValueVisitor visitValue(String key);
    
    void visitEnd();
    
}
