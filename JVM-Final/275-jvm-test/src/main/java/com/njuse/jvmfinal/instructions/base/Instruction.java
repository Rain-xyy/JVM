package com.njuse.jvmfinal.instructions.base;

import com.njuse.jvmfinal.runtime.StackFrame;

import java.nio.ByteBuffer;

public abstract class Instruction {
    public Instruction() {
    }

    public abstract void execute(StackFrame stackFrame);

    public abstract void fetchOperands(ByteBuffer byteBuffer);
}
