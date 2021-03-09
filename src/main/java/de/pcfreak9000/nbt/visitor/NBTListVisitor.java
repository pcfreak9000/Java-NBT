package de.pcfreak9000.nbt.visitor;

import de.pcfreak9000.nbt.NBTType;

public interface NBTListVisitor {
    
    void visitType(NBTType type);
    
    void visitLength(int l);
    
    NBTValueVisitor visitValue();
    
    void visitEnd();
    
}
