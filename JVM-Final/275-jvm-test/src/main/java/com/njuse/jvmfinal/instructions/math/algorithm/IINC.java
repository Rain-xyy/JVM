package com.njuse.jvmfinal.instructions.math.algorithm;

import com.njuse.jvmfinal.instructions.base.Instruction;
import com.njuse.jvmfinal.runtime.StackFrame;

import java.nio.ByteBuffer;

public class IINC extends Instruction {
    private int index;
    private int add_const;
    @Override
    public void execute(StackFrame frame) {
        int value = frame.getLocalVars().getInt(this.index);
        value += this.add_const;
        frame.getLocalVars().setInt(index, value);
    }

    @Override
    public void fetchOperands(ByteBuffer reader) {
        this.index = reader.get() & 255;
        this.add_const = reader.get();
    }
}
