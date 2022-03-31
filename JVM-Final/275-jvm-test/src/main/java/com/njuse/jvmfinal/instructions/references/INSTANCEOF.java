package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.ClassRef;
import com.njuse.jvmfinal.runtime.JObject;
import com.njuse.jvmfinal.runtime.StackFrame;

public class INSTANCEOF extends Index16Instruction {
    @Override
    public void execute(StackFrame frame) {
        JObject objectRef = frame.getOperandStack().popObjectRef();
        if(objectRef == null || objectRef.isNull()){
            frame.getOperandStack().pushInt(0);
        }else {
            ClassRef ref = (ClassRef) frame.getMethod().getClazz().getRuntimeConstantPool().getConstant(index);
            try {
                JClass T = ref.getResolvedClass();
                if(objectRef.isInstanceOf(T)){
                    frame.getOperandStack().pushInt(1);
                }else{
                    frame.getOperandStack().pushInt(0);
                }
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }
}
