package com.njuse.seecjvm.instructions.invoke;

import com.njuse.seecjvm.instructions.base.Index16Instruction;
import com.njuse.seecjvm.memory.jclass.InitState;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.Method;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref.InterfaceMethodRef;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref.MemberRef;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref.MethodRef;
import com.njuse.seecjvm.runtime.StackFrame;
import com.njuse.seecjvm.runtime.Vars;
import com.njuse.seecjvm.runtime.struct.Slot;


public class INVOKE_STATIC extends Index16Instruction {

    /**
     * TODO 实现这条指令，注意其中的非标准部分：
     * 1. TestUtil.equalInt(int a, int b): 如果a和b相等，则跳过这个方法，
     * 否则抛出`RuntimeException`, 其中，这个异常的message为
     * ：${第一个参数的值}!=${第二个参数的值}
     * 例如，TestUtil.equalInt(1, 2)应该抛出
     * RuntimeException("1!=2")
     *
     * 2. TestUtil.fail(): 抛出`RuntimeException`
     *
     * 3. TestUtil.equalFloat(float a, float b): 如果a和b相等，则跳过这个方法，
     * 否则抛出`RuntimeException`. 对于异常的message不作要求
     *
     */
    @Override
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

        //针对非标准部分的特判
            if(((MemberRef) ref).className.equals("TestUtil")) {
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
                    case "fail":
                        throw new RuntimeException();
                    case "equalFloat": {
                        float arg2 = frame.getOperandStack().popFloat();
                        float arg1 = frame.getOperandStack().popFloat();
                        if (arg1 == arg2) {
                            frame.getOperandStack().pushFloat(arg1);
                            frame.getOperandStack().pushFloat(arg2);
                        } else throw new RuntimeException(arg1 + "!=" + arg2);
                        break;
                    }
                }
            }


        if(method.getClazz().getInitState() == InitState.PREPARED){
            frame.setNextPC(frame.getNextPC() - 3);
            method.getClazz().initClass(frame.getThread(), method.getClazz());
            return;
        }


        //参数出栈
        Slot[] args = copyArguments(frame, method);
        StackFrame newFrame = new StackFrame(frame.getThread(), method,
                method.getMaxStack(), method.getMaxLocal() + 1);
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
