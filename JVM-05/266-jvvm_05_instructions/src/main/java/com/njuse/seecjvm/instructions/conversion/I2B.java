package com.njuse.seecjvm.instructions.conversion;

import com.njuse.seecjvm.instructions.base.NoOperandsInstruction;
import com.njuse.seecjvm.runtime.StackFrame;

public class I2B extends NoOperandsInstruction {

    /**
     * TODO：（加分项）实现这条指令
     * 这是一条可选测试用例才会涉及的指令
     */
    @Override
    public void execute(StackFrame frame) {
        byte value = (byte)frame.getOperandStack().popInt();
        //默认将value符号扩展为int类型
        frame.getOperandStack().pushInt(value);
    }
}
