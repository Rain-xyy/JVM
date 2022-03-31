package com.njuse.jvmfinal.instructions.math.shift;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class ISHL extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        int val2 = frame.getOperandStack().popInt();
        int val1 = frame.getOperandStack().popInt();
        val2 = val2 & 0x1f;
        frame.getOperandStack().pushInt((int) (val1 * Math.pow(2, val2)));
    }
}
