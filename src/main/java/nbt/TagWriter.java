package nbt;

import nbt.NBTTag.ByteEntry;
import nbt.NBTTag.DoubleEntry;
import nbt.NBTTag.EndEntry;
import nbt.NBTTag.FloatEntry;
import nbt.NBTTag.IntEntry;
import nbt.NBTTag.LongEntry;
import nbt.NBTTag.ShortEntry;
import nbt.NBTTag.StringEntry;
import nbt.visitor.NBTCompoundVisitor;
import nbt.visitor.NBTListVisitor;
import nbt.visitor.NBTValueVisitor;

public class TagWriter implements NBTValueVisitor {
    
    private NBTTag entry;
    
    protected void set(NBTTag entry) {
        this.entry = entry;
    }
    
    public NBTTag get() {
        return entry;
    }
    
    @Override
    public void visitString(String s) {
        set(new StringEntry(s));
    }
    
    @Override
    public void visitInt(int i) {
        set(new IntEntry(i));
    }
    
    @Override
    public void visitEnd() {
        set(EndEntry.END_ENTRY);
    }
    
    @Override
    public NBTCompoundVisitor visitCompound() {
        return new CompoundWriter() {
            @Override
            protected void set(NBTCompound comp) {
                TagWriter.this.set(comp);
            }
        };
    }
    
    @Override
    public NBTListVisitor visitList() {
        return new ListWriter() {
            
            @Override
            protected void set(NBTList list) {
                TagWriter.this.set(list);
            }
        };
    }
    
    @Override
    public void visitByte(byte b) {
        set(new ByteEntry(b));
    }
    
    @Override
    public void visitShort(short s) {
        set(new ShortEntry(s));
    }
    
    @Override
    public void visitLong(long l) {
        set(new LongEntry(l));
    }
    
    @Override
    public void visitFloat(float f) {
        set(new FloatEntry(f));
    }
    
    @Override
    public void visitDouble(double d) {
        set(new DoubleEntry(d));
    }
    
    private static abstract class CompoundWriter implements NBTCompoundVisitor {
        
        private NBTCompound compound = new NBTCompound();
        
        public CompoundWriter() {
        }
        
        @Override
        public NBTValueVisitor visitValue(String key) {
            return new TagWriter() {
                @Override
                protected void set(NBTTag entry) {
                    CompoundWriter.this.compound.put(key, entry);
                }
            };
        }
        
        @Override
        public void visitEnd() {
            this.set(compound);
        }
        
        protected abstract void set(NBTCompound comp);
    }
    
    private static abstract class ListWriter implements NBTListVisitor {
        private NBTList list;
        private int length;
        
        public ListWriter() {
        }
        
        @Override
        public void visitType(NBTType type) {
            if (list != null) {
                throw new IllegalStateException("Found a second type");
            }
            list = new NBTList(type);
        }
        
        @Override
        public void visitLength(int l) {
            this.length = l;
        }
        
        @Override
        public NBTValueVisitor visitValue() {
            return new TagWriter() {
                @Override
                protected void set(NBTTag entry) {
                    ListWriter.this.list.add(entry);
                }
            };
        }
        
        @Override
        public void visitEnd() {
            if (length != list.getContent().size()) {
                throw new IllegalStateException("List size changed");
            }
            this.set(list);
        }
        
        protected abstract void set(NBTList list);
    }
}
