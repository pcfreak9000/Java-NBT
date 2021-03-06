package nbt.visitor;

public abstract class AbstractNBTCompoundVisitor implements NBTCompoundVisitor {
    
    private final NBTCompoundVisitor parent;
    
    public AbstractNBTCompoundVisitor(NBTCompoundVisitor parent) {
        this.parent = parent;
    }
    
    @Override
    public NBTValueVisitor visitValue(String key) {
        if (parent != null) {
            return parent.visitValue(key);
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
