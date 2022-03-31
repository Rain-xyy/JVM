package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.runtime.StackFrame;

public class FLOAD_N extends LOAD_N {
    public FLOAD_N(int index) {
        checkIndex(index);
        this.index = index;
    }

    public void execute(StackFrame frame) {
        float val = frame.getLocalVars().getFloat(this.index);
        frame.getOperandStack().pushFloat(val);
    }
}
