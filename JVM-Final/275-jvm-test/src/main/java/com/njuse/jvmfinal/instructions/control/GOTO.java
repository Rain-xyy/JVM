package com.njuse.jvmfinal.instructions.control;

import com.njuse.jvmfinal.instructions.base.BranchInstruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class GOTO extends BranchInstruction {
    public GOTO() {
    }

    public void execute(StackFrame frame) {
        int branchPC = frame.getNextPC() - 3 + super.offset;
        frame.setNextPC(branchPC);
    }
}
