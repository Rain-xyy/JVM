package com.njuse.jvmfinal.instructions.stack;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.Slot;
import com.njuse.jvmfinal.runtime.StackFrame;

public class DUP extends NoOperandsInstruction {
    public DUP() {
    }

    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        Slot slot = stack.popSlot();
        stack.pushSlot(slot);
        stack.pushSlot(slot.clone());
    }
}
