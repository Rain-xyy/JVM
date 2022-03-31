package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.array.CharArrayObject;

public class CASTORE extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        char val = (char) frame.getOperandStack().popInt();
        int index = frame.getOperandStack().popInt();
        CharArrayObject charArrayObject = (CharArrayObject) frame.getOperandStack().popObjectRef();
        if(charArrayObject == null){
            throw new NullPointerException();
        }else if(index < 0 || index >= charArrayObject.getLen()){
            throw new ArrayIndexOutOfBoundsException();
        }else{
            charArrayObject.getArray()[index] = val;
        }
    }
}
