package com.njuse.seecjvm.instructions.conversion;

import com.njuse.seecjvm.instructions.base.NoOperandsInstruction;
import com.njuse.seecjvm.runtime.StackFrame;

public class I2L extends NoOperandsInstruction {

    /**
     * TODO：（加分项）实现这条指令
     * 这是一条可选测试用例才会涉及的指令
     */
    @Override
    public void execute(StackFrame frame) {
        long value = frame.getOperandStack().popInt();
        frame.getOperandStack().pushLong(value);
    }
}
