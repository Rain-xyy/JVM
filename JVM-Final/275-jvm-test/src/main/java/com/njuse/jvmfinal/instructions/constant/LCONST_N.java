package com.njuse.jvmfinal.instructions.constant;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class LCONST_N extends NoOperandsInstruction {
    private long val;
    private static long[] valid = new long[]{0L, 1L};

    public LCONST_N(long index) {
        for (long v : valid) {
            if (v - index <= 0.0001) {
                this.val = index;
                return;
            }
        }
    }

    public void execute(StackFrame frame) {
        frame.getOperandStack().pushLong(this.val);
    }
}
