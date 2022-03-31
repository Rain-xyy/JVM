package com.njuse.jvmfinal.runtime.array;


import com.njuse.jvmfinal.runtime.ArrayObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FloatArrayObject extends ArrayObject {
    private float[] array;

    public FloatArrayObject(int len, String type) {
        super(len, type);
        array = new float[len];
    }
}
