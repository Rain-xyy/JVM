package com.njuse.jvmfinal.instructions.base;

import java.nio.ByteBuffer;

public abstract class BranchInstruction extends Instruction {
    protected int offset;

    public BranchInstruction() {
    }

    public void fetchOperands(ByteBuffer reader) {
        this.offset = reader.getShort();
    }

    public String toString() {
        return this.getClass().getSimpleName() + " offset: " + this.offset;
    }
}
