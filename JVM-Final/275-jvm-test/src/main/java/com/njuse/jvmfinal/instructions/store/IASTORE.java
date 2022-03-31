package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.array.IntArrayObject;

public class IASTORE extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        int val = frame.getOperandStack().popInt();
        int index = frame.getOperandStack().popInt();
        IntArrayObject arrayObject = (IntArrayObject) frame.getOperandStack().popObjectRef();
        if(arrayObject == null){
            throw new NullPointerException();
        }else if(index < 0 || index >= arrayObject.getLen()){
            throw new ArrayIndexOutOfBoundsException();
        }else{
            arrayObject.getArray()[index] = val;
        }
    }
}
