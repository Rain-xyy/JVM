package com.njuse.jvmfinal.runtime;

import com.njuse.jvmfinal.memory.jclass.Method;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StackFrame {
    private OperandStack operandStack;
    private Vars localVars;
    private JThread thread;
    private int nextPC;
    private Method method;

    public StackFrame(JThread thread, Method method, int maxStackSize, int maxVarSize) {
        this.thread = thread;
        this.method = method;
        operandStack = new OperandStack(maxStackSize);
        localVars = new Vars(maxVarSize);
    }

    public OperandStack getOperandStack() {
        return this.operandStack;
    }

    public Vars getLocalVars() {
        return this.localVars;
    }

    public JThread getThread() {
        return this.thread;
    }

    public int getNextPC() {
        return this.nextPC;
    }

    public Method getMethod() {
        return this.method;
    }

    public void setOperandStack(OperandStack operandStack) {
        this.operandStack = operandStack;
    }

    public void setLocalVars(Vars localVars) {
        this.localVars = localVars;
    }

    public void setThread(JThread thread) {
        this.thread = thread;
    }

    public void setNextPC(int nextPC) {
        this.nextPC = nextPC;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
