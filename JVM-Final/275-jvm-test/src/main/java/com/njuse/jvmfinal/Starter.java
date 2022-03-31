package com.njuse.jvmfinal;


import com.njuse.jvmfinal.execution.Interpreter;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.runtime.JThread;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.ClassFileReader.ClassFileReader;
import com.njuse.jvmfinal.ClassFileReader.ClassLoader;

import java.io.File;

public class Starter {
    public static void main(String[] args) {

    }

    /**
     * ⚠️警告：不要改动这个方法签名，这是和测试用例的唯一接口
     */
    public static void runTest(String mainClassName, String cp){
        String JAVA_HOME = System.getenv("JAVA_HOME");
        try {
            ClassFileReader.setBootClasspath(String.join(File.separator,JAVA_HOME,"jre","lib","*"));
            ClassFileReader.setExtClasspath(String.join(File.separator,JAVA_HOME,"jre","lib","ext", "*"));
            ClassFileReader.setUserClasspath(cp);
            ClassLoader loader = ClassLoader.getInstance();
            //this clazz is the initial class of java virtual machine, so we should initialize it firth,
            //after which we should call the main method
            JClass clazz = loader.loadClass(mainClassName, null);
            JThread thread = new JThread();
            Method main = clazz.getMainMethod();
            StackFrame mainFrame = new StackFrame(thread, main, main.getMaxStack(), main.getMaxLocal());
            thread.pushFrame(mainFrame);
            clazz.initClass(thread, clazz);
            Interpreter.interpret(thread);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
