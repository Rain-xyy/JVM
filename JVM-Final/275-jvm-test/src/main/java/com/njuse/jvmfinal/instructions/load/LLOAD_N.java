package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.runtime.StackFrame;

public class LLOAD_N extends LOAD_N {
    public LLOAD_N(int index) {
        checkIndex(index);
        this.index = index;
    }

    public void execute(StackFrame frame) {
        long val = frame.getLocalVars().getLong(this.index);
        frame.getOperandStack().pushLong(val);
    }
}
