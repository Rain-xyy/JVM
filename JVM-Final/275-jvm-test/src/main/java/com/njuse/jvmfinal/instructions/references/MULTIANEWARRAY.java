package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.instructions.base.Instruction;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.JHeap;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.ClassRef;
import com.njuse.jvmfinal.runtime.ArrayObject;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.array.RefArrayObject;

import java.nio.ByteBuffer;

public class MULTIANEWARRAY extends Instruction {
    private int index;
    private int dimensions;
    @Override
    public void execute(StackFrame frame) {
        ClassRef classRef = (ClassRef) frame.getMethod().getClazz().getRuntimeConstantPool().getConstant(this.index);
        try {
            JClass arrClass = classRef.getResolvedClass();
            OperandStack stack = frame.getOperandStack();
            //获取各维度的长度
            int[] lenArr = this.popAndCheck(stack, this.dimensions);
            ArrayObject ref = this.createMultiDimensionArray(0, lenArr, arrClass);
            //将ref对应的实例压入堆区
            JHeap.getInstance().addObj(ref);
            //将ref入到操作数栈
            stack.pushObjectRef(ref);
        } catch (ClassNotFoundException var8) {
            var8.printStackTrace();
        }
    }

    private int[] popAndCheck(OperandStack stack, int dimensions) {
        int[] lenArr = new int[dimensions];

        for(int i = dimensions - 1; i >= 0; --i) {
            lenArr[i] = stack.popInt();
            if (lenArr[i] < 0) {
                throw new NegativeArraySizeException();
            }
        }

        return lenArr;
    }

    //递归创建多维数组
    private ArrayObject createMultiDimensionArray(int index, int[] lenArray, JClass arrClass) {
        int len = lenArray[index];
        index++;
        ArrayObject arr = arrClass.newArrayObject(len);
        if (index <= lenArray.length - 1) {
            assert arr instanceof RefArrayObject;

            for(int i = 0; i < arr.getLen(); ++i) {
                ((RefArrayObject)arr).getArray()[i] = this.createMultiDimensionArray(index, lenArray, arrClass.getComponentClass());
            }
        }

        return arr;
    }

    @Override
    public void fetchOperands(ByteBuffer reader) {
        this.index = reader.getShort() & 0xffff;
        this.dimensions = reader.get() & 0xff;
        assert dimensions >= 1;
    }
}
