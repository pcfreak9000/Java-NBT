package de.pcfreak9000.nbt;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.pcfreak9000.nbt.util.AbstractWriterHelper;
import de.pcfreak9000.nbt.visitor.AbstractNBTValueVisitor;
import de.pcfreak9000.nbt.visitor.NBTCompoundVisitor;
import de.pcfreak9000.nbt.visitor.NBTListVisitor;
import de.pcfreak9000.nbt.visitor.NBTValueVisitor;

public class NbtWriter extends AbstractNBTValueVisitor implements Closeable, Flushable {
    
    private static final int TMP_BUFFER_LENGTH = 8192;
    
    private final DataOutputStream stream;
    private final List<IOException> exceptionsCatched;
    
    //name==null => no type prefix (type and name) will be written
    
    public NbtWriter(OutputStream out) {
        this(out, "");
    }
    
    public NbtWriter(OutputStream out, String name) {
        this(name, out instanceof DataOutputStream ? (DataOutputStream) out : new DataOutputStream(out),
                new ArrayList<>(1));
    }
    
    private NbtWriter(String name, DataOutputStream s, List<IOException> exceptionsCatched) {
        super(new ValueWriter(name, s, exceptionsCatched));
        this.exceptionsCatched = exceptionsCatched;
        this.stream = s;
    }
    
    @Override
    public void flush() throws IOException {
        this.stream.flush();
    }
    
    @Override
    public void close() throws IOException {
        this.closeParentQuietly();
        this.throwException();
    }
    
    private void closeParentQuietly() {
        try {
            this.stream.close();
        } catch (IOException e) {
            this.exceptionsCatched.add(e);
        }
    }
    
    private void throwException() throws IOException {
        if (!this.exceptionsCatched.isEmpty()) {
            IOException first = this.exceptionsCatched.get(0);
            for (int i = 1; i < this.exceptionsCatched.size(); i++) {
                first.addSuppressed(this.exceptionsCatched.get(i));
            }
            throw first;
        }
    }
    
    private static class ValueWriter extends AbstractWriterHelper<DataOutputStream> implements NBTValueVisitor {
        
        private final String name;
        
        public ValueWriter(String name, DataOutputStream stream, List<IOException> catched) {
            super(stream, catched);
            this.name = name;
        }
        
        private void writeString(String s) throws IOException {
            byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
            this.dataOut.writeShort(bytes.length);
            this.dataOut.write(bytes);
        }
        
        private void writePrefix(NBTType type) throws IOException {
            if (this.name != null) {
                this.dataOut.writeByte(type.id);
                if (type != NBTType.End) {
                    this.writeString(this.name);
                }
            }
        }
        
        @Override
        public NBTCompoundVisitor visitCompound() {
            run(() -> writePrefix(NBTType.Compound));
            return new CompoundWriter(dataOut, supressedExceptions);
        }
        
        @Override
        public void visitEnd() {
            run(() -> writePrefix(NBTType.End));
        }
        
        @Override
        public void visitInt(int i) {
            run(() -> writePrefix(NBTType.Int));
            run(() -> this.dataOut.writeInt(i));
        }
        
        @Override
        public void visitString(String s) {
            run(() -> writePrefix(NBTType.String));
            run(() -> writeString(s));
        }
        
        @Override
        public void visitByte(byte b) {
            run(() -> writePrefix(NBTType.Byte));
            run(() -> this.dataOut.writeByte(b));
        }
        
        @Override
        public void visitShort(short s) {
            run(() -> writePrefix(NBTType.Short));
            run(() -> this.dataOut.writeShort(s));
        }
        
        @Override
        public void visitLong(long l) {
            run(() -> writePrefix(NBTType.Long));
            run(() -> this.dataOut.writeLong(l));
        }
        
        @Override
        public void visitFloat(float f) {
            run(() -> writePrefix(NBTType.Float));
            run(() -> this.dataOut.writeFloat(f));
        }
        
        @Override
        public void visitDouble(double d) {
            run(() -> writePrefix(NBTType.Double));
            run(() -> this.dataOut.writeDouble(d));
        }
        
        @Override
        public NBTListVisitor visitList() {
            run(() -> writePrefix(NBTType.List));
            return new ListWriter(dataOut, supressedExceptions);
        }
        
        @Override
        public void visitByteArray(byte[] ar) {
            run(() -> {
                writePrefix(NBTType.ByteArray);
                int length = ar.length;
                this.dataOut.writeInt(length);
                byte[] bufferArray = new byte[TMP_BUFFER_LENGTH];
                ByteBuffer buffer = ByteBuffer.wrap(bufferArray);
                for (int bufferLimit = buffer.limit(), offset = 0,
                        bufferStep; (bufferStep = Math.min(bufferLimit, length - offset)) > 0; offset += bufferStep) {
                    buffer.put(Arrays.copyOfRange(ar, offset, offset + bufferStep));
                    this.dataOut.write(bufferArray, 0, bufferArray.length * bufferStep / bufferLimit);
                    buffer.rewind();
                }
            });
        }
        
        @Override
        public void visitIntArray(int[] ar) {
            run(() -> {
                writePrefix(NBTType.IntArray);
                int length = ar.length;
                this.dataOut.writeInt(length);
                byte[] bufferArray = new byte[TMP_BUFFER_LENGTH];
                IntBuffer buffer = ByteBuffer.wrap(bufferArray).asIntBuffer();
                for (int bufferLimit = buffer.limit(), offset = 0,
                        bufferStep; (bufferStep = Math.min(bufferLimit, length - offset)) > 0; offset += bufferStep) {
                    buffer.put(Arrays.copyOfRange(ar, offset, offset + bufferStep));
                    this.dataOut.write(bufferArray, 0, bufferArray.length * bufferStep / bufferLimit);
                    buffer.rewind();
                }
            });
        }
        
        @Override
        public void visitLongArray(long[] ar) {
            run(() -> {
                writePrefix(NBTType.LongArray);
                int length = ar.length;
                this.dataOut.writeInt(length);
                byte[] bufferArray = new byte[TMP_BUFFER_LENGTH];
                LongBuffer buffer = ByteBuffer.wrap(bufferArray).asLongBuffer();
                for (int bufferLimit = buffer.limit(), offset = 0,
                        bufferStep; (bufferStep = Math.min(bufferLimit, length - offset)) > 0; offset += bufferStep) {
                    buffer.put(Arrays.copyOfRange(ar, offset, offset + bufferStep));
                    this.dataOut.write(bufferArray, 0, bufferArray.length * bufferStep / bufferLimit);
                    buffer.rewind();
                }
            });
        }
        
    }
    
    private static class ListWriter extends AbstractWriterHelper<DataOutputStream> implements NBTListVisitor {
        
        //This would probably handle cases where values are visited before the length has been visited, but why should that happen?
        //private ByteArrayOutputStream tmp;
        
        public ListWriter(DataOutputStream s, List<IOException> es) {
            super(s, es);
            //tmp = new ByteArrayOutputStream(0);
        }
        
        @Override
        public void visitType(NBTType type) {
            run(() -> dataOut.writeByte(type.id));
        }
        
        @Override
        public void visitLength(int l) {
            run(() -> {
                dataOut.writeInt(l);
                //                if (tmp != null) {
                //                    byte[] ar = tmp.toByteArray();
                //                    dataOut.write(ar);
                //                    tmp.close();
                //                    tmp = null;
                //                }
            });
            
        }
        
        @Override
        public NBTValueVisitor visitValue() {
            //   DataOutputStream output = this.tmp != null ? new DataOutputStream(tmp) : dataOut;
            return new ValueWriter(null, dataOut, supressedExceptions);
        }
        
        @Override
        public void visitEnd() {
            //we dont do this, because this is handled via the list length
            //run(() -> this.dataOut.writeByte(NBTType.End.id));
        }
    }
    
    private static class CompoundWriter extends AbstractWriterHelper<DataOutputStream> implements NBTCompoundVisitor {
        
        public CompoundWriter(DataOutputStream s, List<IOException> es) {
            super(s, es);
        }
        
        @Override
        public NBTValueVisitor visitValue(String key) {
            return new ValueWriter(key, dataOut, supressedExceptions);
        }
        
        @Override
        public void visitEnd() {
            run(() -> this.dataOut.writeByte(NBTType.End.id));
        }
        
    }
    
}
