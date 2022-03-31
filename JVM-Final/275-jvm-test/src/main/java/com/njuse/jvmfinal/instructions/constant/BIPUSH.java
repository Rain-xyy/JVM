package com.njuse.jvmfinal.instructions.constant;

import com.njuse.jvmfinal.instructions.base.Instruction;
import com.njuse.jvmfinal.runtime.StackFrame;

import java.nio.ByteBuffer;

public class BIPUSH extends Instruction {
    private byte val;

    public BIPUSH() {
    }

    public void fetchOperands(ByteBuffer reader) {
        this.val = reader.get();
    }

    public void execute(StackFrame frame) {
        frame.getOperandStack().pushInt(this.val);
    }
}
