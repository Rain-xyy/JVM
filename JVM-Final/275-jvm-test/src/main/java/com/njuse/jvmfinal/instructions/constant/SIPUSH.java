package com.njuse.jvmfinal.instructions.constant;

import com.njuse.jvmfinal.instructions.base.Instruction;
import com.njuse.jvmfinal.runtime.StackFrame;

import java.nio.ByteBuffer;

public class SIPUSH extends Instruction {
    private short val;

    public SIPUSH() {
    }

    public void fetchOperands(ByteBuffer reader) {
        this.val = reader.getShort();
    }

    public void execute(StackFrame frame) {
        frame.getOperandStack().pushInt(this.val);
    }
}