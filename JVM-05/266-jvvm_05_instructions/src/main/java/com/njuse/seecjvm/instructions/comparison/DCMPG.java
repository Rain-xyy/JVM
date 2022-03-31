package com.njuse.seecjvm.instructions.comparison;

import com.njuse.seecjvm.instructions.base.NoOperandsInstruction;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;

public class DCMPG extends NoOperandsInstruction {

    /**
     * TODO：实现这条指令
     */
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        double val2 = stack.popDouble();
        double val1 = stack.popDouble();
        if (Double.isNaN(val1) || Double.isNaN(val2)) stack.pushInt(1);
        else if (val1 > val2) stack.pushInt(1);
        else if (val1 == val2) stack.pushInt(0);
        else stack.pushInt(-1);
    }
}
