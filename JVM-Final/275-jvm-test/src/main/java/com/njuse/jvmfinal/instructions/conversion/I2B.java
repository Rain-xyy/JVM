package com.njuse.jvmfinal.instructions.conversion;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class I2B extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        int val = frame.getOperandStack().popInt();
        byte result = (byte) val;
        //默认将value符号扩展为int类型
        frame.getOperandStack().pushInt(result);
    }
}
