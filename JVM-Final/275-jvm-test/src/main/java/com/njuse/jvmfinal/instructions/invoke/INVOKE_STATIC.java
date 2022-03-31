package com.njuse.jvmfinal.instructions.invoke;


import com.njuse.jvmfinal.memory.jclass.InitState;
import com.njuse.jvmfinal.runtime.Slot;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;
import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.InterfaceMethodRef;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.MemberRef;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.MethodRef;

public class INVOKE_STATIC extends Index16Instruction {
    public void execute(StackFrame frame) {
        JClass currentClz = frame.getMethod().getClazz();
        Constant ref = currentClz.getRuntimeConstantPool().getConstant(super.index);
        assert (ref instanceof InterfaceMethodRef || ref instanceof MethodRef);
        Method method;
        if(ref instanceof  InterfaceMethodRef)
            method = ((InterfaceMethodRef) ref).resolveInterfaceMethodRef();
        else
            method = ((MethodRef) ref).resolveMethodRef();

        assert !(method.getName().equals("<init>") || method.getName().equals("<clinit>"));
        assert method.isStatic();

        if(method.getClazz().getInitState() == InitState.PREPARED){
            frame.setNextPC(frame.getNextPC() - 3);
            method.getClazz().initClass(frame.getThread(), method.getClazz());
            return;
        }

        //针对非标准部分的特判
            if(((MemberRef) ref).className.equals("cases/TestUtil")) {
                switch (method.getName()) {
                    case "equalInt": {
                        int arg2 = frame.getOperandStack().popInt();
                        int arg1 = frame.getOperandStack().popInt();
                        if (arg1 == arg2) {
                            //frame.getOperandStack().pushInt(arg1);
                            //frame.getOperandStack().pushInt(arg2);
                            frame.getOperandStack().pushInt(1);
                            return;
                        } else throw new RuntimeException(arg1 + "!=" + arg2);
                        //break;
                    }
                    case "equalFloat": {
                        float arg2 = frame.getOperandStack().popFloat();
                        float arg1 = frame.getOperandStack().popFloat();
                        if (arg1 == arg2) {
                            frame.getOperandStack().pushFloat(arg1);
                            frame.getOperandStack().pushFloat(arg2);
                        } else throw new RuntimeException(arg1 + "!=" + arg2);
                        break;
                    }
                    case "reach": {
                        int arg = frame.getOperandStack().popInt();
                        System.out.println(arg);
                        frame.getOperandStack().pushInt(arg);
                    }
                }
            }

        //参数出栈
        Slot[] args = copyArguments(frame, method);
        StackFrame newFrame = new StackFrame(frame.getThread(), method,
                method.getMaxStack(), method.getMaxLocal());
        Vars localVars = newFrame.getLocalVars();
        int argc = method.getArgc();
        for (int i = 0; i < args.length; i++) {
            localVars.setSlot(i, args[argc - i - 1]);
        }

        frame.getThread().pushFrame(newFrame);

        if (method.isNative()) {
                frame.getThread().popFrame();
        }
    }

    private Slot[] copyArguments(StackFrame frame, Method method) {
        int argc = method.getArgc();
        Slot[] argv = new Slot[argc];
        for (int i = 0; i < argc; i++) {
            argv[i] = frame.getOperandStack().popSlot();
        }
        return argv;
    }


}
