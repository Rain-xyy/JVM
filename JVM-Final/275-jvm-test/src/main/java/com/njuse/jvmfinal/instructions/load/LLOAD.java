package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.instructions.base.Index8Instruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class LLOAD extends Index8Instruction {
    public LLOAD() {
    }

    public void execute(StackFrame frame) {
        long val = frame.getLocalVars().getLong(this.index);
        frame.getOperandStack().pushLong(val);
    }
}
