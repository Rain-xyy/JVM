package com.njuse.jvmfinal.instructions.control;


import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.JThread;
import com.njuse.jvmfinal.runtime.StackFrame;

public class IRETURN extends NoOperandsInstruction {

    public void execute(StackFrame frame) {
        int value = frame.getOperandStack().popInt();
        JThread thread = frame.getThread();
        thread.popFrame();
        thread.getTopFrame().getOperandStack().pushInt(value);
    }
}
