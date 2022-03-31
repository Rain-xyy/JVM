package com.njuse.jvmfinal.instructions.constant;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class ICONST_N extends NoOperandsInstruction {
    private int val;
    private static int[] valid = new int[]{-1, 0, 1, 2, 3, 4, 5};

    public ICONST_N(int index) {
        for (int v : valid) {
            if (v == index) {
                this.val = index;
                return;
            }
        }
    }

    public void execute(StackFrame frame) {
        frame.getOperandStack().pushInt(this.val);
    }
}
