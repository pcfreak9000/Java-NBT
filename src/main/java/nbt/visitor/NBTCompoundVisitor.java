package nbt.visitor;

public interface NBTCompoundVisitor {
    
    NBTValueVisitor visitValue(String key);
    
    void visitEnd();
    
}
