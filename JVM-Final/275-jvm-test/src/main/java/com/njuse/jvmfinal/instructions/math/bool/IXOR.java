package com.njuse.jvmfinal.instructions.math.bool;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class IXOR extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        int val2 = frame.getOperandStack().popInt();
        int val1 = frame.getOperandStack().popInt();
        frame.getOperandStack().pushInt(val1 ^ val2);
    }
}
