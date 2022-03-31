package com.njuse.jvmfinal.instructions.constant;


import com.njuse.jvmfinal.runtime.NullObject;
import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class ACONST_NULL extends NoOperandsInstruction {
    public void execute(StackFrame frame) {
        frame.getOperandStack().pushObjectRef(new NullObject());
    }
}
