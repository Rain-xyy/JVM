package com.njuse.jvmfinal.instructions.comparison;

import com.njuse.jvmfinal.instructions.base.BranchInstruction;
import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.runtime.JObject;
import com.njuse.jvmfinal.runtime.StackFrame;

public class IFNONNULL extends BranchInstruction {
    @Override
    public void execute(StackFrame frame) {
        JObject object = frame.getOperandStack().popObjectRef();
        if(!object.isNull()){
            int branchPC = frame.getNextPC() + offset - 3;
            frame.setNextPC(branchPC);
        }
    }
}
