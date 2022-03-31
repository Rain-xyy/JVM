package com.njuse.jvmfinal.instructions.base;

import java.nio.ByteBuffer;

public abstract class NoOperandsInstruction extends Instruction{
    public NoOperandsInstruction() {
    }

    public void fetchOperands(ByteBuffer reader) {
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
