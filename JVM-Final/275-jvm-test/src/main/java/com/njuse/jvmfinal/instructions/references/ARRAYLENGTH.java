package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.ArrayObject;
import com.njuse.jvmfinal.runtime.StackFrame;

public class ARRAYLENGTH extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        ArrayObject object = (ArrayObject) frame.getOperandStack().popObjectRef();
        if(object == null){
            throw new NullPointerException();
        }else {
            frame.getOperandStack().pushInt(object.getLen());
        }
    }
}
