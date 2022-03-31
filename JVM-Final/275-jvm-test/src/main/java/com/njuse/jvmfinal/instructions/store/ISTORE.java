package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.instructions.base.Index8Instruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class ISTORE extends Index8Instruction {
    public ISTORE() {
    }

    public void execute(StackFrame frame) {
        int val = frame.getOperandStack().popInt();
        frame.getLocalVars().setInt(this.index, val);
    }
}
