package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.array.CharArrayObject;

public class CALOAD extends NoOperandsInstruction {
    public CALOAD() {
    }

    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        int index = stack.popInt();
        CharArrayObject arrRef = (CharArrayObject)stack.popObjectRef();
        if (arrRef == null) {
            throw new NullPointerException();
        } else if (this.checkIndex(arrRef.getLen(), index)) {
            stack.pushInt(arrRef.getArray()[index] & 0xff);
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    private boolean checkIndex(int len, int index) {
        return index >= 0 && index < len;
    }
}
