package com.njuse.jvmfinal.runtime;

import java.util.EmptyStackException;
import java.util.Stack;

public class ThreadStack {
    private static int maxSize = 65536;
    private Stack<StackFrame> stack = new Stack();
    private Stack<Boolean> frameState = new Stack();
    private int currentSize;

    public ThreadStack() {
    }

    public boolean isEmpty() {
        return this.currentSize == 0;
    }

    public void pushFrame(StackFrame frame) {
        if (this.currentSize >= maxSize) {
            throw new StackOverflowError();
        } else {
            this.stack.push(frame);
            this.frameState.push(true);
            ++this.currentSize;
        }
    }

    public void popFrame() {
        if (this.currentSize == 0) {
            throw new EmptyStackException();
        } else {
            this.stack.pop();
            this.frameState.pop();
            --this.currentSize;
        }
    }

    public StackFrame getTopFrame() {
        return this.currentSize == 0 ? null : (StackFrame)this.stack.lastElement();
    }

    public void setStack(Stack<StackFrame> stack) {
        this.stack = stack;
    }

    public void setFrameState(Stack<Boolean> frameState) {
        this.frameState = frameState;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    public Stack<StackFrame> getStack() {
        return this.stack;
    }

    public Stack<Boolean> getFrameState() {
        return this.frameState;
    }

    public int getCurrentSize() {
        return this.currentSize;
    }
}
