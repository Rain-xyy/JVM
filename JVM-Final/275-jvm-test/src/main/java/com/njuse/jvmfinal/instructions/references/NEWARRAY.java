package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.ClassFileReader.classpath.EntryType;
import com.njuse.jvmfinal.instructions.base.Instruction;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.JHeap;
import com.njuse.jvmfinal.runtime.ArrayObject;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.ClassFileReader.ClassLoader;
import java.nio.ByteBuffer;

public class NEWARRAY extends Instruction {
    private int atype;

    public NEWARRAY() {
    }

    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        int count = stack.popInt();
        if (count < 0) {
            throw new NegativeArraySizeException();
        } else {
            //数组类的加载，在方法区创建一个数组的clazz对象
            JClass arrClass = this.getPrimitiveArrayClass(this.atype, frame.getMethod().getClazz().getLoadEntryType());
            //数组创建
            ArrayObject ref = arrClass.newArrayObject(count);
            JHeap.getInstance().addObj(ref);
            stack.pushObjectRef(ref);
        }
    }

    public void fetchOperands(ByteBuffer reader) {
        this.atype = reader.get() & 255;
    }

    private JClass getPrimitiveArrayClass(int atype, EntryType initiatingEntry) {
        try {
            ClassLoader loader = ClassLoader.getInstance();
            switch(atype) {
                case 4:
                    return loader.loadClass("[Z", initiatingEntry);
                case 5:
                    return loader.loadClass("[C", initiatingEntry);
                case 6:
                    return loader.loadClass("[F", initiatingEntry);
                case 7:
                    return loader.loadClass("[D", initiatingEntry);
                case 8:
                    return loader.loadClass("[B", initiatingEntry);
                case 9:
                    return loader.loadClass("[S", initiatingEntry);
                case 10:
                    return loader.loadClass("[I", initiatingEntry);
                case 11:
                    return loader.loadClass("[J", initiatingEntry);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Invalid atype!");
    }
}
