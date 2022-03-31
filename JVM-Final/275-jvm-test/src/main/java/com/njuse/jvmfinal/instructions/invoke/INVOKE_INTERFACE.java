package com.njuse.jvmfinal.instructions.invoke;


import com.njuse.jvmfinal.runtime.JObject;
import com.njuse.jvmfinal.runtime.Slot;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;
import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.InterfaceMethodRef;

import java.nio.ByteBuffer;

public class INVOKE_INTERFACE extends Index16Instruction {

    @Override
    public void fetchOperands(ByteBuffer reader) {
        this.index = reader.getShort() & '\uffff';
        int count = reader.get() & 0xff;
        int zero = reader.get();
    }

    public void execute(StackFrame frame) {
        JClass currentClz = frame.getMethod().getClazz();
        Constant interfaceMethodRef = currentClz.getRuntimeConstantPool().getConstant(super.index);
        assert interfaceMethodRef instanceof InterfaceMethodRef;
        Method method = ((InterfaceMethodRef) interfaceMethodRef).resolveInterfaceMethodRef();

        //参数出栈
        int argc = method.getArgc();
        Slot[] argv = new Slot[argc];
        for (int i = 0; i < argc; i++) {
            argv[i] = frame.getOperandStack().popSlot();
        }

        //objectRef出栈
        JObject objectRef = frame.getOperandStack().popObjectRef();
        JClass clazz = objectRef.getClazz();
        Method toInvoke = ((InterfaceMethodRef) interfaceMethodRef).resolveInterfaceMethodRef(clazz);


        //为被调用方法创建栈帧
        StackFrame newFrame = prepareNewFrame(frame, argc, argv, objectRef, toInvoke);

        frame.getThread().pushFrame(newFrame);

        if (method.isNative()) {
            frame.getThread().popFrame();
        }
    }

    private StackFrame prepareNewFrame(StackFrame frame, int argc, Slot[] argv, JObject objectRef, Method toInvoke) {
        StackFrame newFrame = new StackFrame(frame.getThread(), toInvoke,
                toInvoke.getMaxStack(), toInvoke.getMaxLocal() + 1);
        Vars localVars = newFrame.getLocalVars();
        Slot thisSlot = new Slot();
        thisSlot.setObject(objectRef);
        localVars.setSlot(0, thisSlot);
        for (int i = 1; i < argc + 1; i++) {
            localVars.setSlot(i, argv[argc - i]);
        }
        return newFrame;
    }


}
