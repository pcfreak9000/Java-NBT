package de.pcfreak9000.nbt;

import java.util.Arrays;

import de.pcfreak9000.nbt.visitor.NBTValueVisitor;

public abstract class NBTTag {
    
    private final NBTType type;
    
    public NBTTag(NBTType type) {
        this.type = type;
    }
    
    public void accept(NBTValueVisitor visitor) {
        //This default is kinda expensive...
        TagReader.applyVisitor(visitor, this);
    }
    
    public NBTType type() {
        return this.type;
    }
    
    public abstract NBTTag cpy();
    
    public static class ByteEntry extends NBTTag {
        
        private final byte i;
        
        public ByteEntry(byte i) {
            super(NBTType.Byte);
            this.i = i;
        }
        
        public byte getByte() {
            return i;
        }
        
        @Override
        public void accept(NBTValueVisitor visitor) {
            visitor.visitByte(i);
        }
        
        @Override
        public ByteEntry cpy() {
            return new ByteEntry(i);
        }
    }
    
    public static class ShortEntry extends NBTTag {
        
        private final short i;
        
        public ShortEntry(short i) {
            super(NBTType.Short);
            this.i = i;
        }
        
        public short getShort() {
            return i;
        }
        
        @Override
        public void accept(NBTValueVisitor visitor) {
            visitor.visitShort(i);
        }
        
        @Override
        public ShortEntry cpy() {
            return new ShortEntry(i);
        }
    }
    
    public static class IntEntry extends NBTTag {
        
        private final int i;
        
        public IntEntry(int i) {
            super(NBTType.Int);
            this.i = i;
        }
        
        public int getInt() {
            return i;
        }
        
        @Override
        public void accept(NBTValueVisitor visitor) {
            visitor.visitInt(i);
        }
        
        @Override
        public IntEntry cpy() {
            return new IntEntry(i);
        }
    }
    
    public static class LongEntry extends NBTTag {
        
        private final long i;
        
        public LongEntry(long i) {
            super(NBTType.Long);
            this.i = i;
        }
        
        public long getLong() {
            return i;
        }
        
        @Override
        public void accept(NBTValueVisitor visitor) {
            visitor.visitLong(i);
        }
        
        @Override
        public LongEntry cpy() {
            return new LongEntry(i);
        }
    }
    
    public static class FloatEntry extends NBTTag {
        
        private final float i;
        
        public FloatEntry(float i) {
            super(NBTType.Float);
            this.i = i;
        }
        
        public float getFloat() {
            return i;
        }
        
        @Override
        public void accept(NBTValueVisitor visitor) {
            visitor.visitFloat(i);
        }
        
        @Override
        public FloatEntry cpy() {
            return new FloatEntry(i);
        }
    }
    
    public static class DoubleEntry extends NBTTag {
        
        private final double i;
        
        public DoubleEntry(double i) {
            super(NBTType.Double);
            this.i = i;
        }
        
        public double getDouble() {
            return i;
        }
        
        @Override
        public void accept(NBTValueVisitor visitor) {
            visitor.visitDouble(i);
        }
        
        @Override
        public DoubleEntry cpy() {
            return new DoubleEntry(i);
        }
    }
    
    public static class ByteArrayEntry extends NBTTag {
        
        private final byte[] value;
        
        public ByteArrayEntry(byte[] value) {
            super(NBTType.ByteArray);
            this.value = value;
        }
        
        public byte[] getBytes() {
            return value;
        }
        
        @Override
        public void accept(NBTValueVisitor visitor) {
            visitor.visitByteArray(this.value);
        }
        
        @Override
        public ByteArrayEntry cpy() {
            return new ByteArrayEntry(Arrays.copyOf(value, value.length));//Hmmm... continue with the original byte[] or copy it?
        }
    }
    
    public static class IntArrayEntry extends NBTTag {
        
        private final int[] value;
        
        public IntArrayEntry(int[] value) {
            super(NBTType.IntArray);
            this.value = value;
        }
        
        public int[] getInts() {
            return value;
        }
        
        @Override
        public void accept(NBTValueVisitor visitor) {
            visitor.visitIntArray(this.value);
        }
        
        @Override
        public IntArrayEntry cpy() {
            return new IntArrayEntry(Arrays.copyOf(value, value.length));
        }
    }
    
    public static class LongArrayEntry extends NBTTag {
        
        private final long[] value;
        
        public LongArrayEntry(long[] value) {
            super(NBTType.LongArray);
            this.value = value;
        }
        
        public long[] getLongs() {
            return value;
        }
        
        @Override
        public void accept(NBTValueVisitor visitor) {
            visitor.visitLongArray(this.value);
        }
        
        @Override
        public LongArrayEntry cpy() {
            return new LongArrayEntry(Arrays.copyOf(value, value.length));
        }
    }
    
    public static class StringEntry extends NBTTag {
        
        private final String string;
        
        public StringEntry(String string) {
            super(NBTType.String);
            this.string = string;
        }
        
        public String getString() {
            return string;
        }
        
        @Override
        public void accept(NBTValueVisitor visitor) {
            visitor.visitString(string);
        }
        
        @Override
        public StringEntry cpy() {
            return new StringEntry(string);
        }
        
        public static String escape(String s) {
            StringBuilder builder = new StringBuilder(" ");
            char backslash = '\\', singleQuote = '\'', doubleQuote = '"';
            char quotation = 0;
            for (int i = 0; i < s.length(); ++i) {
                char current = s.charAt(i);
                if (current == backslash) {
                    builder.append(backslash);
                } else if (current == doubleQuote || current == singleQuote) {
                    if (quotation == 0) {
                        quotation = current == doubleQuote ? singleQuote : doubleQuote;
                    }
                    if (quotation == current) {
                        builder.append(backslash);
                    }
                }
                builder.append(current);
            }
            if (quotation == 0) {
                quotation = doubleQuote;
            }
            builder.setCharAt(0, quotation);
            builder.append(quotation);
            return builder.toString();
        }
    }
    
    public static class EndEntry extends NBTTag {
        
        public static final EndEntry END_ENTRY = new EndEntry();
        
        private EndEntry() {
            super(NBTType.End);
        }
        
        @Override
        public void accept(NBTValueVisitor visitor) {
            visitor.visitEnd();
        }
        
        @Override
        public NBTTag cpy() {
            return new EndEntry();
        }
    }
}
