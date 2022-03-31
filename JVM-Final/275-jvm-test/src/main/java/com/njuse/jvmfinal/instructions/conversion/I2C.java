package com.njuse.jvmfinal.instructions.conversion;


import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class I2C extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        int val = frame.getOperandStack().popInt();
        byte value = (byte)val;
        frame.getOperandStack().pushInt(((int) value) & 0xff);
    }
}
