package nbt.visitor;

public interface NBTValueVisitor {
    
    void visitEnd();
    
    void visitByte(byte b);
    
    void visitShort(short s);
    
    void visitInt(int i);
    
    void visitLong(long l);
    
    void visitFloat(float f);
    
    void visitDouble(double d);
    
    void visitString(String s);
    
    NBTCompoundVisitor visitCompound();

    NBTListVisitor visitList();
    
}
