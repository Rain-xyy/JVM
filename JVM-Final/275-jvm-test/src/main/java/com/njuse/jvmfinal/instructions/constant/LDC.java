package com.njuse.jvmfinal.instructions.constant;

import com.njuse.jvmfinal.instructions.base.Index8Instruction;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.FloatWrapper;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.IntWrapper;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class LDC extends Index8Instruction {
    public LDC() {
    }

    public void execute(StackFrame frame) {
        loadConstant(frame, this.index);
    }

    public static void loadConstant(StackFrame frame, int index) {
        OperandStack stack = frame.getOperandStack();
        Constant constant = frame.getMethod().getClazz().getRuntimeConstantPool().getConstant(index);
        if (constant instanceof IntWrapper) {
            stack.pushInt(((IntWrapper) constant).getValue());
        }
        else if (constant instanceof FloatWrapper) {
            stack.pushFloat(((FloatWrapper) constant).getValue());
        }

        else throw new ClassFormatError();

    }
}

