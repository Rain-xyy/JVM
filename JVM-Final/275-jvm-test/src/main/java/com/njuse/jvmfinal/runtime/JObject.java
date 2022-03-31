package com.njuse.jvmfinal.runtime;

import com.njuse.jvmfinal.memory.jclass.JClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class JObject {
    protected static int numInHeap;
    protected int id;
    protected JClass clazz;
    protected boolean isNull = false;

    static {
        numInHeap = 0;
    }

    public JObject() {
        id = numInHeap;
    }

    public boolean isInstanceOf(JClass T) {
        //to judge whether clazz T is the Superclass of the clazz that objRef points to
        return T.isAssignableFrom(this.clazz);
    }

}
