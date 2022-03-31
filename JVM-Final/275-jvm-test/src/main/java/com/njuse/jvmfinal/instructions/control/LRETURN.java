package com.njuse.jvmfinal.instructions.control;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.JThread;
import com.njuse.jvmfinal.runtime.StackFrame;

public class LRETURN extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        long value = frame.getOperandStack().popLong();
        JThread thread = frame.getThread();
        thread.popFrame();
        thread.getTopFrame().getOperandStack().pushLong(value);
    }
}
