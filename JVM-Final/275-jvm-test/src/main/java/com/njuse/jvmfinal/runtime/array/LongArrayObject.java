package com.njuse.jvmfinal.runtime.array;


import com.njuse.jvmfinal.runtime.ArrayObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LongArrayObject extends ArrayObject {
    private long[] array;

    public LongArrayObject(int len, String type) {
        super(len, type);
        array = new long[len];
    }
}