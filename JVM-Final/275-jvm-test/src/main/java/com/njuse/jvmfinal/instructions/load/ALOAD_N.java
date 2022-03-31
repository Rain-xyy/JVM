package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.runtime.JObject;
import com.njuse.jvmfinal.runtime.StackFrame;

public class ALOAD_N extends LOAD_N {
    public ALOAD_N(int index) {
        checkIndex(index);
        this.index = index;
    }

    public void execute(StackFrame frame) {
        JObject ref = frame.getLocalVars().getObjectRef(this.index);
        frame.getOperandStack().pushObjectRef(ref);
    }
}