package com.njuse.jvmfinal.instructions.control;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.JThread;
import com.njuse.jvmfinal.runtime.StackFrame;

public class DRETURN extends NoOperandsInstruction {
    public void execute(StackFrame frame) {
        double value = frame.getOperandStack().popDouble();
        JThread thread = frame.getThread();
        thread.popFrame();
        thread.getTopFrame().getOperandStack().pushDouble(value);
    }
}
