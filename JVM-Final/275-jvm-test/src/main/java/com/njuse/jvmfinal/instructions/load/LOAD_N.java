package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;

public abstract class LOAD_N extends NoOperandsInstruction {
    protected int index;
    private static int[] valid = new int[]{0, 1, 2, 3};

    public LOAD_N() {
    }

    public static void checkIndex(int i) {
        assert i >= valid[0] && i <= valid[valid.length - 1];
    }
}