package com.njuse.jvmfinal.instructions.base;

import java.nio.ByteBuffer;

public abstract class Index16Instruction extends Instruction {
    public int index;

    public Index16Instruction() {
    }

    public void fetchOperands(ByteBuffer reader) {
        this.index = reader.getShort() & 0xffff;
    }

    public String toString() {
        return this.getClass().getSimpleName() + " index: " + (this.index & 0xffff);
    }
}
