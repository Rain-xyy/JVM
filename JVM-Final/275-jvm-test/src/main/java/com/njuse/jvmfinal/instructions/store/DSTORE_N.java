package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.runtime.StackFrame;

public class DSTORE_N extends STORE_N {
    public DSTORE_N(int index){
        checkIndex(index);
        this.index = index;
    }
    @Override
    public void execute(StackFrame frame) {
        double val = frame.getOperandStack().popDouble();
        frame.getLocalVars().setDouble(index, val);
    }
}
