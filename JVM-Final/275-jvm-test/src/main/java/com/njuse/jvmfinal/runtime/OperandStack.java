package com.njuse.jvmfinal.runtime;

import java.util.EmptyStackException;

public class OperandStack {
    private int top;
    private int maxStackSize;
    private Slot[] slots;

    public OperandStack(int maxStackSize) {
        assert maxStackSize >= 0;

        this.maxStackSize = maxStackSize;
        this.slots = new Slot[maxStackSize];

        for(int i = 0; i < maxStackSize; ++i) {
            this.slots[i] = new Slot();
        }

        this.top = 0;
    }

    public void pushInt(int value) {
        if (this.top >= this.maxStackSize) {
            throw new StackOverflowError();
        } else {
            this.slots[this.top].setValue(value);
            ++this.top;
        }
    }

    public int popInt() {
        --this.top;
        if (this.top < 0) {
            throw new EmptyStackException();
        } else {
            int ret = this.slots[this.top].getValue();
            this.slots[this.top] = new Slot();
            return ret;
        }
    }

    public void pushFloat(float value) {
        if (this.top >= this.maxStackSize) {
            throw new StackOverflowError();
        } else {
            this.slots[this.top].setValue(Float.floatToIntBits(value));
            ++this.top;
        }
    }

    public float popFloat() {
        --this.top;
        if (this.top < 0) {
            throw new EmptyStackException();
        } else {
            float ret = Float.intBitsToFloat(this.slots[this.top].getValue());
            this.slots[this.top] = new Slot();
            return ret;
        }
    }

    public void pushLong(long value) {
        if (this.top + 1 >= this.maxStackSize) {
            throw new StackOverflowError();
        } else {
            int low = (int)value;
            int high = (int)(value >> 32);
            this.slots[this.top].setValue(low);
            this.slots[this.top + 1].setValue(high);
            this.top += 2;
        }
    }

    public long popLong() {
        this.top -= 2;
        if (this.top < 0) {
            throw new EmptyStackException();
        } else {
            int low = this.slots[this.top].getValue();
            int high = this.slots[this.top + 1].getValue();
            this.slots[this.top] = new Slot();
            this.slots[this.top + 1] = new Slot();
            return (long)high << 32 | (long)low & 4294967295L;
        }
    }

    public void pushDouble(double value) {
        this.pushLong(Double.doubleToLongBits(value));
    }

    public double popDouble() {
        return Double.longBitsToDouble(this.popLong());
    }

    public void pushObjectRef(JObject ref) {
        if (this.top >= this.maxStackSize) {
            throw new StackOverflowError();
        } else {
            this.slots[this.top].setObject(ref);
            ++this.top;
        }
    }

    public JObject popObjectRef() {
        --this.top;
        if (this.top < 0) {
            throw new EmptyStackException();
        } else {
            JObject ret = this.slots[this.top].getObject();
            this.slots[this.top] = new Slot();
            return ret;
        }
    }

    public void pushSlot(Slot slot) {
        if (this.top >= this.maxStackSize) {
            throw new StackOverflowError();
        } else {
            this.slots[this.top] = slot;
            ++this.top;
        }
    }

    public Slot popSlot() {
        --this.top;
        if (this.top < 0) {
            throw new EmptyStackException();
        } else {
            Slot ret = this.slots[this.top];
            this.slots[this.top] = new Slot();
            return ret;
        }
    }

    public int getTop() {
        return this.top;
    }

    public int getMaxStackSize() {
        return this.maxStackSize;
    }

    public Slot[] getSlots() {
        return this.slots;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public void setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
    }

    public void setSlots(Slot[] slots) {
        this.slots = slots;
    }
}
