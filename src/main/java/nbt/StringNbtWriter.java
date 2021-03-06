package nbt;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import nbt.NBTTag.StringEntry;
import nbt.util.AbstractWriterHelper;
import nbt.visitor.AbstractNBTValueVisitor;
import nbt.visitor.NBTCompoundVisitor;
import nbt.visitor.NBTListVisitor;
import nbt.visitor.NBTValueVisitor;

public class StringNbtWriter extends AbstractNBTValueVisitor implements Closeable, Flushable {
    
    private final Writer writer;
    private final List<IOException> exceptionsCatched;
    
    public StringNbtWriter(Writer out) {
        this(out, new ArrayList<>(1));
    }
    
    private StringNbtWriter(Writer s, List<IOException> exceptionsCatched) {
        super(new ValueWriter(s, exceptionsCatched));
        this.exceptionsCatched = exceptionsCatched;
        this.writer = s;
    }
    
    @Override
    public void flush() throws IOException {
        this.writer.flush();
    }
    
    @Override
    public void close() throws IOException {
        this.closeParentQuietly();
        this.throwException();
    }
    
    private void closeParentQuietly() {
        try {
            this.writer.close();
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
    
    private static class ValueWriter extends AbstractWriterHelper<Writer> implements NBTValueVisitor {
        
        public ValueWriter(Writer s, List<IOException> es) {
            super(s, es);
        }
        
        @Override
        public void visitString(String s) {
            run(() -> dataOut.write(StringEntry.escape(s)));
        }
        
        @Override
        public void visitInt(int i) {
            run(() -> dataOut.write(i + ""));
        }
        
        @Override
        public void visitEnd() {
            if (this.supressedExceptions.isEmpty()) {
                this.supressedExceptions.add(new IOException("String representation doesn't allow end tags"));
            }
        }
        
        @Override
        public NBTCompoundVisitor visitCompound() {
            return new CompoundWriter(dataOut, supressedExceptions);
        }
        
        @Override
        public void visitByte(byte b) {
            run(() -> dataOut.write(b + "b"));
        }
        
        @Override
        public void visitShort(short s) {
            run(() -> dataOut.write(s + "s"));
        }
        
        @Override
        public void visitLong(long l) {
            run(() -> dataOut.write(l + "l"));
        }
        
        @Override
        public void visitFloat(float f) {
            run(() -> dataOut.write(f + "f"));
        }
        
        @Override
        public void visitDouble(double d) {
            run(() -> dataOut.write(d + "d"));
        }
        
        @Override
        public NBTListVisitor visitList() {
            return new ListWriter(dataOut, supressedExceptions);
        }
        
    }
    
    private static final class ListWriter extends AbstractWriterHelper<Writer> implements NBTListVisitor {
        
        private boolean first = true;
        
        public ListWriter(Writer data, List<IOException> suppressed) {
            super(data, suppressed);
        }
        
        @Override
        public void visitType(NBTType type) {
        }
        
        @Override
        public void visitLength(int l) {
        }
        
        @Override
        public NBTValueVisitor visitValue() {
            run(() -> {
                dataOut.write(first ? "[" : ",");
                first = false;
            });
            return new ValueWriter(dataOut, supressedExceptions);
        }
        
        @Override
        public void visitEnd() {
            run(() -> dataOut.write(first ? "[]" : "]"));
        }
    }
    
    private static final class CompoundWriter extends AbstractWriterHelper<Writer> implements NBTCompoundVisitor {
        
        private static final Pattern SIMPLE_KEY = Pattern.compile("[A-Za-z0-9._+-]+");
        
        private boolean notFirst;
        
        public CompoundWriter(Writer data, List<IOException> suppressed) {
            super(data, suppressed);
        }
        
        @Override
        public NBTValueVisitor visitValue(String key) {
            run(() -> {
                String escapedKey = SIMPLE_KEY.matcher(key).matches() ? key : StringEntry.escape(key);
                this.dataOut.write(this.notFirst ? "," : "{");
                this.dataOut.write(escapedKey);
                this.dataOut.write(":");
                this.notFirst = true;
            });
            return new ValueWriter(this.dataOut, this.supressedExceptions);
        }
        
        @Override
        public void visitEnd() {
            run(() -> this.dataOut.write(this.notFirst ? "}" : "{}"));
        }
    }
}
