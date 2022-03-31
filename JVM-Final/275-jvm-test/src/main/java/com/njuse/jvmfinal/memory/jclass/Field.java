package com.njuse.jvmfinal.memory.jclass;

import com.njuse.jvmfinal.classloader.classfileparser.FieldInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class Field extends ClassMember {
    private int slotID;
    private int constValueIndex;

    public Field(FieldInfo info, JClass clazz) {
        this.clazz = clazz;
        accessFlags = info.getAccessFlags();
        name = info.getName();
        descriptor = info.getDescriptor();
        if (info.getConstantValueAttr() != null) {
            constValueIndex = info.getConstantValueAttr().getConstantValueIndex();
        }
    }

    public static String parseDescriptor(String descriptor) {
        switch(descriptor.charAt(0)) {
            case 'B':
                return "byte";
            case 'C':
                return "char";
            case 'D':
                return "double";
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            default:
                throw new RuntimeException("Invalid field descriptor.");
            case 'F':
                return "float";
            case 'I':
                return "int";
            case 'J':
                return "long";
            case 'L':
                String name = descriptor.substring(1, descriptor.length() - 1);
                String[] tmp = name.split("/");
                name = tmp[tmp.length - 1];
                return name;
            case 'S':
                return "short";
            case 'Z':
                return "boolean";
            case '[':
                return parseArrayName(descriptor);
        }
    }

    private static String parseArrayName(String descriptor) {
        int dimensions = (int) Arrays.stream(descriptor.split("")).filter((ch) -> {
            return ch.equals("[");
        }).count();
        String type = descriptor.substring(descriptor.lastIndexOf("[") + 1);
        StringBuilder ret = new StringBuilder(parseDescriptor(type));

        for(int i = 0; i < dimensions; ++i) {
            ret.append("[]");
        }

        return ret.toString();
    }

    public int getSlotID() {
        return this.slotID;
    }

    public int getConstValueIndex() {
        return this.constValueIndex;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }

    public void setConstValueIndex(int constValueIndex) {
        this.constValueIndex = constValueIndex;
    }

}
