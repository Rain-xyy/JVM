package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.runtime.StackFrame;

public class ISTORE_N extends STORE_N {
    public ISTORE_N(int index) {
        checkIndex(index);
        this.index = index;
    }

    public void execute(StackFrame frame) {
        int val = frame.getOperandStack().popInt();
        frame.getLocalVars().setInt(this.index, val);
    }
}
