package de.pcfreak9000.nbt;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import de.pcfreak9000.nbt.util.ContextStackEvaluator;
import de.pcfreak9000.nbt.util.ContextStackEvaluator.ContextType;
import de.pcfreak9000.nbt.util.ContextStackEvaluator.IContext;
import de.pcfreak9000.nbt.util.ImmutableBytes;
import de.pcfreak9000.nbt.util.ImmutableInts;
import de.pcfreak9000.nbt.util.ImmutableLongs;
import de.pcfreak9000.nbt.visitor.NBTCompoundVisitor;
import de.pcfreak9000.nbt.visitor.NBTListVisitor;
import de.pcfreak9000.nbt.visitor.NBTValueVisitor;

public class NbtReader implements Closeable {
    
    private final DataInputStream stream;
    private final NBTType initialType;
    private final String initialName;
    
    public NbtReader(InputStream instream) throws IOException {
        this.stream = instream instanceof DataInputStream ? (DataInputStream) instream : new DataInputStream(instream);
        initialType = nextType();
        initialName = initialType != NBTType.End ? nextString() : "";
    }
    
    public void applyVisitor(NBTValueVisitor visitor) throws IOException {
        ContextStackEvaluator.evaluate(new ValueContext(visitor, initialType));
    }
    
    @Override
    public void close() throws IOException {
        this.stream.close();
    }
    
    public String getName() {
        if (this.initialType == NBTType.End) {
            throw new NoSuchElementException("End tags do not have a name");
        }
        return this.initialName;
    }
    
    public NBTCompound toCompoundTag() throws IOException {
        NBTTag tag = toTag();
        if (tag.type() != NBTType.Compound) {
            throw new IOException("Unexpected type");
        }
        return (NBTCompound) tag;
    }
    
    public NBTTag toTag() throws IOException {
        TagWriter writer = new TagWriter();
        this.applyVisitor(writer);
        return writer.get();
    }
    
    private NBTType nextType() throws IOException {
        int abyte = this.stream.readByte();
        try {
            return NBTType.getById(abyte);
        } catch (NoSuchElementException ex) {
            throw new IOException("Unknown type: " + abyte, ex);
        }
    }
    
    private String nextString() throws IOException {
        int length = this.stream.readShort() & 0xFFFF;
        byte[] bytes = new byte[length];
        this.stream.readFully(bytes, 0, length);
        return new String(bytes, StandardCharsets.UTF_8);
    }
    
    private class ValueContext implements IContext {
        private final NBTValueVisitor valueVisitor;
        private final NBTType nbtType;
        
        public ValueContext(NBTValueVisitor valueVisitor, NBTType nbtType) {
            this.valueVisitor = valueVisitor;
            this.nbtType = nbtType;
        }
        
        @Override
        public IContext evaluate() throws IOException {
            switch (nbtType) {
            case Compound:
                return new CompoundContext(valueVisitor.visitCompound());
            case End:
                valueVisitor.visitEnd();
                break;
            case Byte:
                valueVisitor.visitByte(stream.readByte());
                break;
            case Short:
                valueVisitor.visitShort(stream.readShort());
                break;
            case Int:
                valueVisitor.visitInt(stream.readInt());
                break;
            case Long:
                valueVisitor.visitLong(stream.readLong());
                break;
            case Float:
                valueVisitor.visitFloat(stream.readFloat());
                break;
            case Double:
                valueVisitor.visitDouble(stream.readDouble());
                break;
            case String:
                valueVisitor.visitString(nextString());
                break;
            case ByteArray: {
                int len = stream.readInt();
                if (len >= 0 && len <= 0x7FFFFFF7) {
                    byte[] bytes = new byte[len];
                    stream.readFully(bytes);
                    valueVisitor.visitByteArray(new ImmutableBytes(bytes));
                    break;
                }
                throw new IOException("Size exceeds " + 0x7FFFFFF7 + ", got " + (len & 0xFFFFFFFFL));
            }
            case IntArray: {
                int len = stream.readInt();
                if (len >= 0 && len <= 0x7FFFFFF7) {
                    byte[] bufferArray = new byte[8192];
                    int[] ints = new int[len];
                    int fillIndex = 0;
                    IntBuffer buffer = ByteBuffer.wrap(bufferArray).asIntBuffer();
                    for (int bufferLimit = buffer.limit(), offset = 0,
                            bufferStep; (bufferStep = Math.min(bufferLimit, len - offset)) > 0; offset += bufferStep) {
                        stream.readFully(bufferArray, 0, bufferArray.length * bufferStep / bufferLimit);
                        buffer.limit(bufferStep);
                        int remaining = buffer.remaining();
                        buffer.get(ints, fillIndex, remaining);
                        fillIndex += remaining;
                        buffer.clear();
                    }
                    valueVisitor.visitIntArray(new ImmutableInts(ints));
                    break;
                }
                throw new IOException("Size exceeds " + 0x7FFFFFF7 + ", got " + (len & 0xFFFFFFFFL));
            }
            case LongArray: {
                int len = stream.readInt();
                if (len >= 0 && len <= 0x7FFFFFF7) {
                    byte[] bufferArray = new byte[8192];
                    long[] longs = new long[len];
                    int fillIndex = 0;
                    LongBuffer buffer = ByteBuffer.wrap(bufferArray).asLongBuffer();
                    for (int bufferLimit = buffer.limit(), offset = 0,
                            bufferStep; (bufferStep = Math.min(bufferLimit, len - offset)) > 0; offset += bufferStep) {
                        stream.readFully(bufferArray, 0, bufferArray.length * bufferStep / bufferLimit);
                        buffer.limit(bufferStep);
                        int remaining = buffer.remaining();
                        buffer.get(longs, fillIndex, remaining);
                        fillIndex += remaining;
                        buffer.clear();
                    }
                    valueVisitor.visitLongArray(new ImmutableLongs(longs));
                    break;
                }
                throw new IOException("Size exceeds " + 0x7FFFFFF7 + ", got " + (len & 0xFFFFFFFFL));
            }
            case List:
                NBTType type = nextType();
                int size = stream.readInt();
                if (type != NBTType.End || size <= 0) {
                    NBTListVisitor listVisitor = valueVisitor.visitList();
                    listVisitor.visitType(type);
                    listVisitor.visitLength(size);
                    return new ListContext(listVisitor, type, size);
                }
                throw new IOException("Lists may not contain end tags");
            default:
                throw new IllegalArgumentException("Type: " + nbtType);
            }
            return null;
        }
        
        @Override
        public ContextType type() {
            return ContextType.Flat;
        }
    }
    
    private class ListContext implements IContext {
        private final NBTListVisitor listVisitor;
        private final NBTType listType;
        private final AtomicInteger left;
        
        public ListContext(NBTListVisitor listVisitor, NBTType listType, int len) {
            this.listVisitor = listVisitor;
            this.listType = listType;
            this.left = new AtomicInteger(len);
        }
        
        @Override
        public IContext evaluate() throws IOException {
            if (left.getAndDecrement() <= 0) {
                listVisitor.visitEnd();
                return null;
            }
            return new ValueContext(listVisitor.visitValue(), listType);
        }
        
        @Override
        public ContextType type() {
            return ContextType.Recursive;
        }
    }
    
    private class CompoundContext implements IContext {
        
        private final NBTCompoundVisitor compoundVisitor;
        
        public CompoundContext(NBTCompoundVisitor compoundVisitor) {
            this.compoundVisitor = compoundVisitor;
        }
        
        @Override
        public IContext evaluate() throws IOException {
            NBTType nextType = nextType();
            if (nextType == NBTType.End) {
                this.compoundVisitor.visitEnd();
                return null;
            }
            return new ValueContext(this.compoundVisitor.visitValue(nextString()), nextType);
        }
        
        @Override
        public ContextType type() {
            return ContextType.Recursive;
        }
    }
}
