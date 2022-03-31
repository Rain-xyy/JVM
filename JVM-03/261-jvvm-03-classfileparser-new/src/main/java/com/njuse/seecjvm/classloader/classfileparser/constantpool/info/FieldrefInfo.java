package com.njuse.seecjvm.classloader.classfileparser.constantpool.info;

import com.njuse.seecjvm.classloader.classfileparser.constantpool.ConstantPool;
import org.apache.commons.lang3.tuple.Pair;

public class FieldrefInfo extends MemberRefInfo {
    private int classIndex;     //指向常量池中的一个Constant_Class_info
    private int nameAndTypeIndex;   //指向常量池的一个Constant_NameAndType_info

    public FieldrefInfo(ConstantPool constantPool, int classIndex, int nameAndTypeIndex) {
        super(constantPool);
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
        super.tag = ConstantPoolInfo.FIELD_REF;
    }


    public String getClassName() {
        return getClassName(classIndex);
    }


    public Pair<String, String> getNameAndDescriptor() {
        return ((NameAndTypeInfo) myCP.get(nameAndTypeIndex)).getNameAndDescriptor();

    }

}
