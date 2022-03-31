package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.runtime.StackFrame;

public class ILOAD_N extends LOAD_N {
    public ILOAD_N(int index) {
        checkIndex(index);
        this.index = index;
    }

    public void execute(StackFrame frame) {
        int val = frame.getLocalVars().getInt(this.index);
        frame.getOperandStack().pushInt(val);
    }
}