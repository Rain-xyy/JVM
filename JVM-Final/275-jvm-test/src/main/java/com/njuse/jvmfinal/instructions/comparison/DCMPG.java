package com.njuse.jvmfinal.instructions.comparison;


import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class DCMPG extends NoOperandsInstruction {

    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        double val2 = stack.popDouble();
        double val1 = stack.popDouble();
        if (!Double.isNaN(val1) && !Double.isNaN(val2)) {
            frame.getOperandStack().pushInt(Double.compare(val1, val2));
        } else {
            frame.getOperandStack().pushInt(1);
        }

    }
}
