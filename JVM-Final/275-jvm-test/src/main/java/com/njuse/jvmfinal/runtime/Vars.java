package com.njuse.jvmfinal.runtime;

public class Vars {
    private Slot[] varSlots;
    private int maxSize;

    public Vars(int maxVarSize) {
        assert maxVarSize >= 0;

        this.maxSize = maxVarSize;
        this.varSlots = new Slot[maxVarSize];

        for(int i = 0; i < maxVarSize; ++i) {
            this.varSlots[i] = new Slot();
        }

    }

    public void setInt(int index, int value) {
        if (index >= 0 && index < this.maxSize) {
            this.varSlots[index].setValue(value);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public int getInt(int index) {
        if (index >= 0 && index < this.maxSize) {
            return this.varSlots[index].getValue();
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void setFloat(int index, float value) {
        if (index >= 0 && index < this.maxSize) {
            this.varSlots[index].setValue(Float.floatToIntBits(value));
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public float getFloat(int index) {
        if (index >= 0 && index < this.maxSize) {
            return Float.intBitsToFloat(this.varSlots[index].getValue());
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void setLong(int index, long value) {
        if (index >= 0 && index + 1 < this.maxSize) {
            this.varSlots[index].setValue((int)value);
            this.varSlots[index + 1].setValue((int)(value >> 32));
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public long getLong(int index) {
        if (index >= 0 && index + 1 < this.maxSize) {
            int low = this.varSlots[index].getValue();
            int high = this.varSlots[index + 1].getValue();
            return (long)high << 32 | (long)low & 4294967295L;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void setDouble(int index, double value) {
        if (index >= 0 && index + 1 < this.maxSize) {
            this.setLong(index, Double.doubleToLongBits(value));
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public double getDouble(int index) {
        if (index >= 0 && index + 1 < this.maxSize) {
            return Double.longBitsToDouble(this.getLong(index));
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void setObjectRef(int index, JObject ref) {
        if (index >= 0 && index < this.maxSize) {
            this.varSlots[index].setObject(ref);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public JObject getObjectRef(int index) {
        if (index >= 0 && index < this.maxSize) {
            return this.varSlots[index].getObject();
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void setSlot(int index, Slot slot) {
        if (index >= 0 && index < this.maxSize) {
            this.varSlots[index] = slot;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public Slot[] getVarSlots() {
        return this.varSlots;
    }

    public int getMaxSize() {
        return this.maxSize;
    }

    public void setVarSlots(Slot[] varSlots) {
        this.varSlots = varSlots;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
