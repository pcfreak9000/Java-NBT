package nbt.visitor;

import nbt.NBTType;

public class AbstractNBTListVisitor implements NBTListVisitor {
    
    private final NBTListVisitor parent;
    
    public AbstractNBTListVisitor(NBTListVisitor parent) {
        this.parent = parent;
    }
    
    @Override
    public void visitType(NBTType type) {
        if (parent != null) {
            parent.visitType(type);
        }
    }
    
    @Override
    public void visitLength(int l) {
        if (parent != null) {
            parent.visitLength(l);
        }
    }
    
    @Override
    public NBTValueVisitor visitValue() {
        if (parent != null) {
            return parent.visitValue();
        }
        return new AbstractNBTValueVisitor(null) {
        };
    }
    
    @Override
    public void visitEnd() {
        if (parent != null) {
            parent.visitEnd();
        }
    }
    
}
