package com.njuse.jvmfinal.instructions.comparison;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class LCMP extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        long v2 = frame.getOperandStack().popLong();
        long v1 = frame.getOperandStack().popLong();
        int result = Long.compare(v1, v2);
        frame.getOperandStack().pushInt(result);
    }
}
