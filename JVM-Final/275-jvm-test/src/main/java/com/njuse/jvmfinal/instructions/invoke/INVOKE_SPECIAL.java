package com.njuse.jvmfinal.instructions.invoke;


import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.MethodRef;
import com.njuse.jvmfinal.runtime.JObject;
import com.njuse.jvmfinal.runtime.Slot;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;
import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.Constant;

public class INVOKE_SPECIAL extends Index16Instruction {
    public void execute(StackFrame frame) {
        JClass currentClz = frame.getMethod().getClazz();
        Constant methodRef = currentClz.getRuntimeConstantPool().getConstant(super.index);

        assert methodRef instanceof MethodRef;

        Method method = ((MethodRef)methodRef).resolveMethodRef();
        JClass c;
        if (frame.getMethod().getClazz().isAccSuper() && !method.getName().equals("<init>") &&
                ((!method.getClazz().isInterface()) && frame.getMethod().getClazz().isSubClassOf(((MethodRef) methodRef).getClazz()))) {
            c = frame.getMethod().getClazz().getSuperClass();
        } else {
            c = ((MethodRef) methodRef).getClazz();
        }

        Method toInvoke = ((MethodRef)methodRef).resolveMethodRef(c);
        Slot[] args = this.copyArguments(frame, method);
        StackFrame newFrame = new StackFrame(frame.getThread(), toInvoke, toInvoke.getMaxStack(), toInvoke.getMaxLocal());
        Vars localVars = newFrame.getLocalVars();
        JObject thisRef = frame.getOperandStack().popObjectRef();
        if(thisRef == null || thisRef.isNull()){
            throw new NullPointerException();
        }
        Slot slot = new Slot();
        slot.setObject(thisRef);
        localVars.setSlot(0, slot);
        int argc = method.getArgc();

        for(int i = 1; i < args.length + 1; ++i) {
            localVars.setSlot(i, args[argc - i]);
        }

        frame.getThread().pushFrame(newFrame);

        if (method.isNative()) {
            frame.getThread().popFrame();
        }
    }

    private Slot[] copyArguments(StackFrame frame, Method method) {
        int argc = method.getArgc();
        Slot[] argv = new Slot[argc];

        for(int i = 0; i < argc; ++i) {
            argv[i] = frame.getOperandStack().popSlot();
        }

        return argv;
    }
}
