package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.instructions.base.Index8Instruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class DSTORE extends Index8Instruction {
    @Override
    public void execute(StackFrame frame) {
        double val = frame.getOperandStack().popDouble();
        frame.getLocalVars().setDouble(this.index, val);
    }
}
