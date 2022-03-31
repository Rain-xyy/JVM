package com.njuse.jvmfinal.instructions.references;


import com.njuse.jvmfinal.memory.jclass.InitState;
import com.njuse.jvmfinal.runtime.NonArrayObject;
import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.JHeap;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.ClassRef;
import com.njuse.jvmfinal.runtime.StackFrame;

public class NEW extends Index16Instruction {
    public void execute(StackFrame frame) {
        RuntimeConstantPool runtimeConstantPool = frame.getMethod().getClazz().getRuntimeConstantPool();
        ClassRef classRef = (ClassRef) runtimeConstantPool.getConstant(index);
        try{
            JClass clazz = classRef.getResolvedClass();
            if (clazz.getInitState() == InitState.PREPARED) {
                frame.setNextPC(frame.getNextPC() - 3);
                clazz.initClass(frame.getThread(), clazz);
                return;
            }

            if(clazz.isInterface() || clazz.isAbstract()) throw new InstantiationError();
            NonArrayObject objectRef = clazz.newObject();
            //将此实例放入堆中
            JHeap.getInstance().addObj(objectRef);
            //将此实例放入操作数栈中
            frame.getOperandStack().pushObjectRef(objectRef);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
