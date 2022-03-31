package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.JObject;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.array.RefArrayObject;

public class AASTORE extends NoOperandsInstruction {

    public void execute(StackFrame frame) {
        JObject value = frame.getOperandStack().popObjectRef();
        int index = frame.getOperandStack().popInt();
        RefArrayObject arrRef = (RefArrayObject) frame.getOperandStack().popObjectRef();
        if(arrRef == null) {
            throw new NullPointerException();
        }
        else if(index < 0 || index >= arrRef.getLen()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        else {
            arrRef.getArray()[index] = value;
        }
    }
}
