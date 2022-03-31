package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.instructions.base.Index8Instruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class DLOAD extends Index8Instruction {
    public DLOAD() {
    }

    public void execute(StackFrame frame) {
        double val = frame.getLocalVars().getDouble(this.index);
        frame.getOperandStack().pushDouble(val);
    }
}
