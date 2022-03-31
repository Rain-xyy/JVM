package com.njuse.jvmfinal.instructions.base;

import java.nio.ByteBuffer;

public abstract class Index8Instruction extends Instruction {
    public int index;

    public Index8Instruction() {
    }

    public void fetchOperands(ByteBuffer reader) {
        this.index = reader.get() & 255;
    }

    public String toString() {
        return this.getClass().getSimpleName() + " index: " + (this.index & 255);
    }
}
