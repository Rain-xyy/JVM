package com.njuse.jvmfinal.instructions.conversion;


import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class I2D extends NoOperandsInstruction {

    @Override
    public void execute(StackFrame frame) {
        double value =  frame.getOperandStack().popInt();
        frame.getOperandStack().pushDouble(value);
    }
}
