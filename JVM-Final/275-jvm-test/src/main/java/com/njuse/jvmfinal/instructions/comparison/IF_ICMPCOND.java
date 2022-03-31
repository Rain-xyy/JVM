package com.njuse.jvmfinal.instructions.comparison;


import com.njuse.jvmfinal.instructions.base.BranchInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public abstract class IF_ICMPCOND extends BranchInstruction {
    @Override
    //作为父类的方法被各个子类继承
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        int v2 = stack.popInt();
        int v1 = stack.popInt();
        if(condition(v1, v2)){
            int branchPC = frame.getNextPC() - 3 + super.offset;
            frame.setNextPC(branchPC);
        }
    }

    protected abstract boolean condition(int v1, int v2);
}
