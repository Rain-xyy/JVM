package com.njuse.jvmfinal.instructions.conversion;


import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class I2S extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        short value = (short)frame.getOperandStack().popInt();
        frame.getOperandStack().pushInt(value);
    }
}
