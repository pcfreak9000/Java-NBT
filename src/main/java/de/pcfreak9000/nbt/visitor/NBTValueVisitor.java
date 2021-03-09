package de.pcfreak9000.nbt.visitor;

import de.pcfreak9000.nbt.util.ImmutableBytes;
import de.pcfreak9000.nbt.util.ImmutableInts;
import de.pcfreak9000.nbt.util.ImmutableLongs;

public interface NBTValueVisitor {
    
    void visitEnd();
    
    void visitByte(byte b);
    
    void visitShort(short s);
    
    void visitInt(int i);
    
    void visitLong(long l);
    
    void visitFloat(float f);
    
    void visitDouble(double d);
    
    void visitString(String s);
    
    void visitByteArray(ImmutableBytes ar);
    
    void visitIntArray(ImmutableInts ar);
    
    void visitLongArray(ImmutableLongs ar);
    
    NBTCompoundVisitor visitCompound();
    
    NBTListVisitor visitList();
    
}
