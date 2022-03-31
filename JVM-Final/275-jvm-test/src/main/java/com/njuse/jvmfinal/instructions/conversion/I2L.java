package com.njuse.jvmfinal.instructions.conversion;


import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class I2L extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        long value = frame.getOperandStack().popInt();
        frame.getOperandStack().pushLong(value);
    }
}
