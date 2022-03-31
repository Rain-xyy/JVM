package com.njuse.jvmfinal.instructions.comparison;


import com.njuse.jvmfinal.instructions.base.BranchInstruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public abstract class IFCOND extends BranchInstruction {
    @Override
    public void execute(StackFrame frame) {
        int value = frame.getOperandStack().popInt();
        if(condition(value)){
            int branchPC = frame.getNextPC() - 3 + super.offset;
            frame.setNextPC(branchPC);
        }
    }

    protected abstract boolean condition(int value);

}
