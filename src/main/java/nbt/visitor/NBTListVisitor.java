package nbt.visitor;

import nbt.NBTType;

public interface NBTListVisitor {
    
    void visitType(NBTType type);
    
    void visitLength(int l);
    
    NBTValueVisitor visitValue();
    
    void visitEnd();
    
}
