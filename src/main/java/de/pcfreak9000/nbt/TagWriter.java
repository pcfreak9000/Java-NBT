package de.pcfreak9000.nbt;

import de.pcfreak9000.nbt.NBTTag.ByteArrayEntry;
import de.pcfreak9000.nbt.NBTTag.ByteEntry;
import de.pcfreak9000.nbt.NBTTag.DoubleEntry;
import de.pcfreak9000.nbt.NBTTag.EndEntry;
import de.pcfreak9000.nbt.NBTTag.FloatEntry;
import de.pcfreak9000.nbt.NBTTag.IntArrayEntry;
import de.pcfreak9000.nbt.NBTTag.IntEntry;
import de.pcfreak9000.nbt.NBTTag.LongArrayEntry;
import de.pcfreak9000.nbt.NBTTag.LongEntry;
import de.pcfreak9000.nbt.NBTTag.ShortEntry;
import de.pcfreak9000.nbt.NBTTag.StringEntry;
import de.pcfreak9000.nbt.visitor.NBTCompoundVisitor;
import de.pcfreak9000.nbt.visitor.NBTListVisitor;
import de.pcfreak9000.nbt.visitor.NBTValueVisitor;

public class TagWriter implements NBTValueVisitor {
    
    private NBTTag entry;
    
    protected void set(NBTTag entry) {
        this.entry = entry;
    }
    
    public NBTTag get() {
        return entry;
    }
    
    public NBTCompound getCompound() {
        return (NBTCompound) get();
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
    
    @Override
    public void visitByteArray(byte[] ar) {
        set(new ByteArrayEntry(ar));
    }
    
    @Override
    public void visitIntArray(int[] ar) {
        set(new IntArrayEntry(ar));
    }
    
    @Override
    public void visitLongArray(long[] ar) {
        set(new LongArrayEntry(ar));
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
