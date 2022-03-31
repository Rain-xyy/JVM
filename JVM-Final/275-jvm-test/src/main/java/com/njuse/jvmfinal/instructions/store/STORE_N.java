package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;

public abstract class STORE_N extends NoOperandsInstruction {
    protected int index;
    private static int[] valid = new int[]{0, 1, 2, 3};

    public STORE_N() {
    }

    public static void checkIndex(int i) {
        assert i >= valid[0] && i <= valid[valid.length - 1];
    }
}
