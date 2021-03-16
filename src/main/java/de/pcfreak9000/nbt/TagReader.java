package de.pcfreak9000.nbt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import de.pcfreak9000.nbt.util.ContextStackEvaluator;
import de.pcfreak9000.nbt.util.ContextStackEvaluator.ContextType;
import de.pcfreak9000.nbt.util.ContextStackEvaluator.IContext;
import de.pcfreak9000.nbt.visitor.NBTCompoundVisitor;
import de.pcfreak9000.nbt.visitor.NBTListVisitor;
import de.pcfreak9000.nbt.visitor.NBTValueVisitor;

public class TagReader {
    
    private TagReader() {
    }
    
    public static void toCompressedBinaryNBTFile(File target, NBTTag nbt) throws IOException {
        try (CompressedNbtWriter writer = new CompressedNbtWriter(new FileOutputStream(target))) {
            applyVisitor(writer, nbt);
        }
    }
    
    public static void toBinaryNBTFile(File target, NBTTag tag) throws IOException {
        try (NbtWriter writer = new NbtWriter(new FileOutputStream(target))) {
            applyVisitor(writer, tag);
        }
    }
    
    public static void applyVisitor(NBTValueVisitor visitor, NBTTag start) {
        try {
            ContextStackEvaluator.evaluate(new ValueContext(start, visitor));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static class CompoundContext implements IContext {
        private final Iterator<Map.Entry<String, NBTTag>> iterator;
        private final NBTCompoundVisitor compoundVisitor;
        
        public CompoundContext(Iterator<Entry<String, NBTTag>> iterator, NBTCompoundVisitor compoundVisitor) {
            this.iterator = iterator;
            this.compoundVisitor = compoundVisitor;
        }
        
        @Override
        public IContext evaluate() {
            if (!iterator.hasNext()) {
                compoundVisitor.visitEnd();
                return null;
            }
            Map.Entry<String, NBTTag> next = iterator.next();
            return new ValueContext(next.getValue(), compoundVisitor.visitValue(next.getKey()));
        }
        
        @Override
        public ContextType type() {
            return ContextType.Recursive;
        }
    }
    
    private static class ListContext implements IContext {
        
        private final NBTListVisitor listVisitor;
        private final Iterator<NBTTag> iterator;
        
        public ListContext(NBTListVisitor lvis, Iterator<NBTTag> it) {
            this.listVisitor = lvis;
            this.iterator = it;
        }
        
        @Override
        public IContext evaluate() throws IOException {
            if (!iterator.hasNext()) {
                listVisitor.visitEnd();
                return null;
            }
            return new ValueContext(iterator.next(), listVisitor.visitValue());
        }
        
        @Override
        public ContextType type() {
            return ContextType.Recursive;
        }
        
    }
    
    private static class ValueContext implements IContext {
        private final NBTTag entry;
        private final NBTValueVisitor valueVisitor;
        
        public ValueContext(NBTTag entry, NBTValueVisitor valueVisitor) {
            this.entry = entry;
            this.valueVisitor = valueVisitor;
        }
        
        @Override
        public IContext evaluate() {
            switch (entry.type()) {
            case Compound:
                NBTCompound compound = (NBTCompound) entry;
                return new CompoundContext(compound.iterator(), valueVisitor.visitCompound());
            case End:
            case Byte:
            case Short:
            case Int:
            case Long:
            case Float:
            case Double:
            case String:
            case ByteArray:
            case IntArray:
            case LongArray:
                entry.accept(valueVisitor);
                return null;
            case List:
                NBTList list = (NBTList) entry;
                NBTListVisitor listVisitor = valueVisitor.visitList();
                listVisitor.visitType(list.getEntryType());
                listVisitor.visitLength(list.getContent().size());
                return new ListContext(listVisitor, list.getContent().iterator());
            default:
                throw new IllegalArgumentException(entry.type() + "");
            }
        }
        
        @Override
        public ContextType type() {
            return ContextType.Flat;
        }
    }
}
