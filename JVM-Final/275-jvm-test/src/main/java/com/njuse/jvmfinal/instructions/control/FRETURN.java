package com.njuse.jvmfinal.instructions.control;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.JThread;
import com.njuse.jvmfinal.runtime.StackFrame;

public class FRETURN extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        float value = frame.getOperandStack().popFloat();
        JThread thread = frame.getThread();
        thread.popFrame();
        thread.getTopFrame().getOperandStack().pushFloat(value);
    }
}
