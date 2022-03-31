package com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref;

import com.njuse.seecjvm.classloader.ClassLoader;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.Constant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SymRef implements Constant {  //clazz是指这个引用所指向的类，className是指向的类的类名，而自己本身的类则通过运行时常量池的getClazz()方法获取
    public RuntimeConstantPool runtimeConstantPool;
    public String className;    //format : java/lang/Object
    public JClass clazz;   //clazz是目标类，也就是SymRef指向的类

    public void resolveClassRef() throws ClassNotFoundException, IllegalAccessException {
        //todo
        /**
         * Add some codes here.
         *
         * You can get a Jclass from runtimeConstantPool.getClazz()
         *
         * step 1
         * Complete the method isAccessibleTo() in Jclass
         * Make sure you know what is caller and what is callee.
         *
         * step2
         * Use ClassLoader.getInstance() to get the classloader
         * You should load class or interface C with initiating Loader of D
         *
         * step3
         * Check the permission and throw IllegalAccessException
         * Don't forget to set the value of clazz with loaded class
         */
        //这个self_clazz是SymRef所属的类，即SymRef物理上在的类（A类引用了B类，SymRef是B类的，但是在A类中被引用，self_clazz是SymRef所在的A类）
        JClass self_clazz = runtimeConstantPool.getClazz();
        ClassLoader classLoader = ClassLoader.getInstance();
        JClass target_clazz = classLoader.loadClass(className, self_clazz.getLoadEntryType());
        if(! target_clazz.isAccessibleTo(self_clazz))
            throw  new IllegalAccessException();
        clazz = target_clazz;
    }
}
