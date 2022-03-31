package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.array.DoubleArrayObject;

public class DALOAD extends NoOperandsInstruction {
    public DALOAD() {
    }

    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        int index = stack.popInt();
        DoubleArrayObject arrRef = (DoubleArrayObject)stack.popObjectRef();
        if (arrRef == null) {
            throw new NullPointerException();
        } else if (this.checkIndex(arrRef.getLen(), index)) {
            stack.pushDouble(arrRef.getArray()[index]);
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    private boolean checkIndex(int len, int index) {
        return index >= 0 && index < len;
    }
}