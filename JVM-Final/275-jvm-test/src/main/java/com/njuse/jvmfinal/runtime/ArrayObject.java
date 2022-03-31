package com.njuse.jvmfinal.runtime;

import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.MethodArea;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArrayObject extends JObject {
    protected int len;
    protected String type;

    public ArrayObject(int len, String type) {
        if (len < 0) throw new NegativeArraySizeException();
        this.len = len;
        this.type = type;
        numInHeap++;
    }

    public boolean isInstanceOf(JClass T) {
        MethodArea methodArea = MethodArea.getInstance();
        switch (this.type) {
            case "[Z":
                this.clazz = methodArea.findClass("[Z");
                break;
            case "[C":
                this.clazz = methodArea.findClass("[C");
                break;
            case "[F":
                this.clazz = methodArea.findClass("[F");
                break;
            case "[D":
                this.clazz = methodArea.findClass("[D");
                break;
            case "[B":
                this.clazz = methodArea.findClass("[B");
                break;
            case "[S":
                this.clazz = methodArea.findClass("[S");
                break;
            case "[I":
                this.clazz = methodArea.findClass("[I");
                break;
            case "[J":
                this.clazz = methodArea.findClass("[J");
                break;
            default:
                throw new NullPointerException("Invalid type");
        }

        //to judge whether clazz T is the Superclass of the clazz that objRef points to
        return T.isAssignableFrom(this.clazz);
    }
}
