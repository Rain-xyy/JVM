package com.njuse.jvmfinal.instructions.comparison;

public class IFGT extends IFCOND {
    @Override
    public boolean condition(int value) {
        return value > 0;
    }
}
