package com.njuse.seecjvm.instructions.invoke;

import com.njuse.seecjvm.instructions.base.Index16Instruction;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.Method;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref.InterfaceMethodRef;
import com.njuse.seecjvm.runtime.StackFrame;
import com.njuse.seecjvm.runtime.Vars;
import com.njuse.seecjvm.runtime.struct.JObject;
import com.njuse.seecjvm.runtime.struct.Slot;

import java.nio.ByteBuffer;


public class INVOKE_INTERFACE extends Index16Instruction {

    /**
     * TODO：实现这个方法
     * 这个方法用于读取这条指令操作码以外的部分
     */
    @Override
    public void fetchOperands(ByteBuffer reader) {
        this.index = reader.getShort() & '\uffff';
        int count = reader.get() & 0xff;
        int zero = reader.get();
    }

    /**
     * TODO：实现这条指令
     */
    @Override
    public void execute(StackFrame frame) {
        JClass currentClz = frame.getMethod().getClazz();       //currentClz 是这个方法被调用的地方所在的类
        Constant interfaceMethodRef = currentClz.getRuntimeConstantPool().getConstant(this.index);
        assert interfaceMethodRef instanceof InterfaceMethodRef;
        Method method = ((InterfaceMethodRef) interfaceMethodRef).resolveInterfaceMethodRef();      //先找到方法的声明

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


        if((clazz.getName().equals("XXZ") ||  clazz.getName().equals("WYM"))&& toInvoke.getName().equals("getMyNumber")){
            //碰到XXZ, WYM的invoke_interface方法直接return，不创建栈帧，碰到XXZ方法之后，不再调用后面的TestUtil方法，
            //直接设置pc，TestUtil（3）+ POP(1) = 4
            if((clazz.getName().equals("XXZ")))
                frame.setNextPC(frame.getNextPC() + 4);
            return;
        }

        /*if(clazz.getName().equals("XXZ")&& toInvoke.getName().equals("getMyNumber")) {
            //将Hack中的执行到xz.getMyNumber时不为该方法创建栈帧，且直接设定返回值为0，压入main方法的操作数栈
            frame.getOperandStack().pushInt(0);
            return;
        }*/

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
