package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.runtime.JObject;
import com.njuse.jvmfinal.instructions.base.Index8Instruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class ASTORE extends Index8Instruction {
    public ASTORE() {
    }

    public void execute(StackFrame frame) {
        JObject ref = frame.getOperandStack().popObjectRef();
        frame.getLocalVars().setObjectRef(this.index, ref);
    }
}
