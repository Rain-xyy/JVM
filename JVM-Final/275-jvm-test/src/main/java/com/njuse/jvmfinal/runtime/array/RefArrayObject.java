package com.njuse.jvmfinal.runtime.array;



import com.njuse.jvmfinal.runtime.ArrayObject;
import com.njuse.jvmfinal.runtime.JObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefArrayObject extends ArrayObject {
    private JObject[] array;

    public RefArrayObject(int len, String type) {
        super(len, type);
        array = new JObject[len];

    }
}
