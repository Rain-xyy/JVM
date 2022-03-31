package com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref;

import com.njuse.jvmfinal.classloader.classfileparser.constantpool.info.MethodrefInfo;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Optional;
import java.util.Stack;

@Getter
@Setter
public class MethodRef extends MemberRef {
    private Method method;

    public MethodRef(RuntimeConstantPool runtimeConstantPool, MethodrefInfo methodrefInfo) {
        super(runtimeConstantPool, methodrefInfo);
    }
    /**
     * TODO：实现这个方法
     * 这个方法用来实现对象方法的动态查找
     * @param clazz 对象的引用
     */
    public Method resolveMethodRef(JClass clazz) {
        this.method = this.resolve(clazz);
        return method;
    }

    /**
     * TODO: 实现这个方法
     * 这个方法用来解析methodRef对应的方法
     * 与上面的动态查找相比，这里的查找始终是从这个Ref对应的class开始查找的
     */
    public Method resolveMethodRef() {
        try {
            //从这个methodRef实际所属的类开始查找
            this.resolveClassRef();
            this.method = this.resolve(clazz);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return method;
    }

    //解析该方法所属的类
    public Method resolve(JClass clazz) {
        assert clazz != null;

        Optional<Method> optionalMethod = Optional.empty();

        //递归(父类中）查找method
        for (JClass currentClazz = clazz;
             currentClazz != null;
             currentClazz = currentClazz.getSuperClass()) {

            optionalMethod = currentClazz.resolveMethod(name, descriptor);

            if (optionalMethod.isPresent()) {
                method = optionalMethod.get();
                return method;
            }

        }

        //if not found in class hierarchy(接口)
        JClass[] ifs = clazz.getInterfaces();
        Stack<JClass> interfaces = new Stack<>();
        interfaces.addAll(Arrays.asList(ifs));
        while (!interfaces.isEmpty()) {
            JClass clz = interfaces.pop();
            optionalMethod = clz.resolveMethod(name, descriptor);
            if (optionalMethod.isPresent()) {
                method = optionalMethod.get();
                return method;
            }
            interfaces.addAll(Arrays.asList(clz.getInterfaces()));
        }
        return null;
    }




    @Override
    public String toString() {
        return "MethodRef to " + className;
    }
}
