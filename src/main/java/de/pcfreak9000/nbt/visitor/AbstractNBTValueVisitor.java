package de.pcfreak9000.nbt.visitor;

import de.pcfreak9000.nbt.util.ImmutableBytes;
import de.pcfreak9000.nbt.util.ImmutableInts;
import de.pcfreak9000.nbt.util.ImmutableLongs;

public class AbstractNBTValueVisitor implements NBTValueVisitor {
    
    protected final NBTValueVisitor parentVisitor;
    
    public AbstractNBTValueVisitor(NBTValueVisitor parent) {
        this.parentVisitor = parent;
    }
    
    @Override
    public void visitString(String s) {
        if (parentVisitor != null) {
            parentVisitor.visitString(s);
        }
    }
    
    @Override
    public void visitInt(int i) {
        if (parentVisitor != null) {
            parentVisitor.visitInt(i);
        }
    }
    
    @Override
    public void visitEnd() {
        if (parentVisitor != null) {
            parentVisitor.visitEnd();
        }
    }
    
    @Override
    public NBTCompoundVisitor visitCompound() {
        if (parentVisitor != null) {
            return parentVisitor.visitCompound();
        }
        return new AbstractNBTCompoundVisitor(null) {
        };
    }
    
    @Override
    public void visitByte(byte b) {
        if (parentVisitor != null) {
            parentVisitor.visitByte(b);
        }
    }
    
    @Override
    public void visitShort(short s) {
        if (parentVisitor != null) {
            parentVisitor.visitShort(s);
        }
    }
    
    @Override
    public void visitLong(long l) {
        if (parentVisitor != null) {
            parentVisitor.visitLong(l);
        }
    }
    
    @Override
    public void visitFloat(float f) {
        if (parentVisitor != null) {
            parentVisitor.visitFloat(f);
        }
    }
    
    @Override
    public void visitDouble(double d) {
        if (parentVisitor != null) {
            parentVisitor.visitDouble(d);
        }
    }
    
    @Override
    public NBTListVisitor visitList() {
        if (parentVisitor != null) {
            return parentVisitor.visitList();
        }
        return new AbstractNBTListVisitor(null) {
        };
    }
    
    @Override
    public void visitByteArray(ImmutableBytes ar) {
        if (parentVisitor != null) {
            parentVisitor.visitByteArray(ar);
        }
    }
    
    @Override
    public void visitIntArray(ImmutableInts ar) {
        if (parentVisitor != null) {
            parentVisitor.visitIntArray(ar);
        }
    }
    
    @Override
    public void visitLongArray(ImmutableLongs ar) {
        if (parentVisitor != null) {
            parentVisitor.visitLongArray(ar);
        }
    }
    
}
