package de.pcfreak9000.nbt.util;

import java.util.StringJoiner;

public class ArrayUtil {
    public static String toString(byte[] ar, String prefix, String delimiter, String suffix) {
        StringJoiner joiner = new StringJoiner(delimiter, prefix, suffix);
        for (int i = 0; i < ar.length; i++) {
            joiner.add(ar[i] + "");
        }
        return joiner.toString();
    }
    
    public static String toString(int[] ar, String prefix, String delimiter, String suffix) {
        StringJoiner joiner = new StringJoiner(delimiter, prefix, suffix);
        for (int i = 0; i < ar.length; i++) {
            joiner.add(ar[i] + "");
        }
        return joiner.toString();
    }
    
    public static String toString(long[] ar, String prefix, String delimiter, String suffix) {
        StringJoiner joiner = new StringJoiner(delimiter, prefix, suffix);
        for (int i = 0; i < ar.length; i++) {
            joiner.add(ar[i] + "");
        }
        return joiner.toString();
    }
    
    public static String toString(byte[] ar) {
        return toString(ar, "[", ",", "]");
    }
    
    public static String toString(int[] ar) {
        return toString(ar, "[", ",", "]");
    }
    
    public static String toString(long[] ar) {
        return toString(ar, "[", ",", "]");
    }
}
