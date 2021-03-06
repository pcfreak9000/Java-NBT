package nbt.util;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import nbt.NBTType;
import nbt.visitor.NBTCompoundVisitor;
import nbt.visitor.NBTListVisitor;
import nbt.visitor.NBTValueVisitor;

public class TestVisitor implements NBTValueVisitor {
    
    private int rec = 0;
    
    private String get() {
        return IntStream.range(0, rec).mapToObj((e) -> " ").collect(Collectors.joining());
    }
    
    @Override
    public void visitString(String string) {
        System.out.println(string);
    }
    
    @Override
    public void visitInt(int i) {
        System.out.println(i);
    }
    
    @Override
    public void visitEnd() {
        System.out.println("End");
    }
    
    @Override
    public NBTCompoundVisitor visitCompound() {
        System.out.println("{");
        rec++;
        return new NBTCompoundVisitor() {
            
            @Override
            public NBTValueVisitor visitValue(String key) {
                System.out.print(get() + "key=" + key + ", value=");
                return TestVisitor.this;
            }
            
            @Override
            public void visitEnd() {
                rec--;
                System.out.println(get() + "}");
            }
        };
    }
    
    @Override
    public void visitByte(byte b) {
        System.out.println(b);
    }
    
    @Override
    public void visitShort(short s) {
        System.out.println(s);
    }
    
    @Override
    public void visitLong(long l) {
        System.out.println(l);
    }
    
    @Override
    public void visitFloat(float f) {
        System.out.println(f);
    }
    
    @Override
    public void visitDouble(double d) {
        System.out.println(d);
    }
    
    @Override
    public NBTListVisitor visitList() {
        System.out.println("[");
        return new NBTListVisitor() {
            
            @Override
            public NBTValueVisitor visitValue() {
                System.out.print(get());
                return TestVisitor.this;
            }
            
            @Override
            public void visitType(NBTType type) {
            }
            
            @Override
            public void visitLength(int l) {
            }
            
            @Override
            public void visitEnd() {
                System.out.println(get()+"]");
            }
        };
    }
    
}
