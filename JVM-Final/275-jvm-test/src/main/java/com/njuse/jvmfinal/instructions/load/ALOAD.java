package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.runtime.JObject;
import com.njuse.jvmfinal.instructions.base.Index8Instruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class ALOAD extends Index8Instruction {
    public ALOAD() {
    }

    public void execute(StackFrame frame) {
        JObject ref = frame.getLocalVars().getObjectRef(this.index);
        frame.getOperandStack().pushObjectRef(ref);
    }
}
