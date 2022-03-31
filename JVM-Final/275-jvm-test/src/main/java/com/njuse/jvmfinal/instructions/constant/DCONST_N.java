package com.njuse.jvmfinal.instructions.constant;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class DCONST_N extends NoOperandsInstruction {
    private double val;
    private static double[] valid = new double[]{0,1};

    public DCONST_N(double val){
        for (double n : valid) {
            if (n - val <= 0.0001) {
                this.val = val;
                return;
            }
        }

        throw new IllegalArgumentException();
    }
    @Override
    public void execute(StackFrame frame) {
        frame.getOperandStack().pushDouble(this.val);
    }
}
