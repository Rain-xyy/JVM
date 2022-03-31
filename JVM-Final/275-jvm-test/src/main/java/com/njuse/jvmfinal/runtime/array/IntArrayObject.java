package com.njuse.jvmfinal.runtime.array;


import com.njuse.jvmfinal.runtime.ArrayObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntArrayObject extends ArrayObject {
    private int[] array;

    public IntArrayObject(int len, String type) {
        super(len, type);
        array = new int[len];
    }
}
