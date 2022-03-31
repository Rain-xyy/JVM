package com.njuse.jvmfinal.instructions.constant;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class FCONST_N extends NoOperandsInstruction {
    private float val;
    private static float [] valid = new float[]{0, 1, 2};

    public FCONST_N(float index){
        for (float v : valid) {
            if (v - index <= 0.0001) {
                this.val = index;
                return;
            }
        }
    }
    @Override
    public void execute(StackFrame frame) {
        frame.getOperandStack().pushFloat(this.val);
    }
}
