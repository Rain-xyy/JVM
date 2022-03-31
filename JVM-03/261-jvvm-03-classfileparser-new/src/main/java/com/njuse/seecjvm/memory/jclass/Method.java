package com.njuse.seecjvm.memory.jclass;

import com.njuse.seecjvm.classloader.classfileparser.MethodInfo;
import com.njuse.seecjvm.classloader.classfileparser.attribute.CodeAttribute;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Method extends ClassMember {
    private int maxStack;   //max_stack表示这个方法运行的任何时刻所能达到的操作栈的最大深度
    private int maxLocal;   //max_locals项的值给出了分配在当前方法引用的局部变量表中的局部变量个数，包括调用此方法时用于传递参数的局部变量
    private int argc;
    private byte[] code;

    public Method(MethodInfo info, JClass clazz) {
        this.clazz = clazz;
        accessFlags = info.getAccessFlags();
        name = info.getName();
        descriptor = info.getDescriptor();

        CodeAttribute codeAttribute = info.getCodeAttribute();
        if (codeAttribute != null) {
            maxLocal = codeAttribute.getMaxLocal();
            maxStack = codeAttribute.getMaxStack();
            code = codeAttribute.getCode();
        }
        argc = calculateArgcFromDescriptor(descriptor);
    }

    //todo calculateArgcFromDescriptor
    private int calculateArgcFromDescriptor(String descriptor) {
        /**
         * Add some codes here.
         * Here are some examples in README!!!
         *
         * You should refer to JVM specification for more details
         *
         * Beware of long and double type
         */
        String str = descriptor.substring(1).split("\\)")[0];
        int i = 0;
        int count = 0;
        while (i < str.length()) {
            switch (str.substring(i, i + 1)) {
                case ("B"):
                case ("C"):
                case ("F"):
                case ("I"):
                case ("S"):
                case ("Z"):
                    count++;
                    break;
                case ("J"):
                case ("D"):
                    count += 2;
                    break;
                case ("L"):
                    count++;
                    i++;
                    while (!str.substring(i, i + 1).equals(";"))
                        i++;
                    break;
                case ("["):
                    /*i++;
                    while(true) {
                        if(str.substring(i, i + 1).equals("["))
                            i++;
                        else if(str.substring(i, i + 1).equals("L")) {
                            while (!str.substring(i, i + 1).equals(";"))
                                i++;
                        }else
                            break;
                    }
                    count++;*/
                    break;
                default:
                    System.out.println("Wrong Descriptor!");
            }
            i++;
        }
        return count;
    }
}
