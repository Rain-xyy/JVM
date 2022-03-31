package com.njuse.seecjvm.classloader;

import com.njuse.seecjvm.classloader.classfileparser.ClassFile;
import com.njuse.seecjvm.classloader.classfilereader.ClassFileReader;
import com.njuse.seecjvm.classloader.classfilereader.classpath.EntryType;
import com.njuse.seecjvm.memory.MethodArea;
import com.njuse.seecjvm.memory.jclass.Field;
import com.njuse.seecjvm.memory.jclass.InitState;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.DoubleWrapper;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.FloatWrapper;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.IntWrapper;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.LongWrapper;
import com.njuse.seecjvm.runtime.Vars;
import com.njuse.seecjvm.runtime.struct.NullObject;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;

public class ClassLoader {
    private static ClassLoader classLoader = new ClassLoader();
    private ClassFileReader classFileReader;
    private MethodArea methodArea;

    private ClassLoader() {
        //得到ClassFileReader对象，MethodArea对象
        classFileReader = ClassFileReader.getInstance();
        methodArea = MethodArea.getInstance();
    }

    public static ClassLoader getInstance() {
        return classLoader;
    }

    /**
     * load phase
     *
     * @param className       name of class
     * @param initiatingEntry null value represents load MainClass
     */
    public JClass loadClass(String className, EntryType initiatingEntry) throws ClassNotFoundException {
        JClass ret;
        //判断有没有被加载过，如果已经被加载过，直接返回ret，否则就加载对应的类
        if ((ret = methodArea.findClass(className)) == null) {
            return loadNonArrayClass(className, initiatingEntry);
        }
        return ret;
    }

    private JClass loadNonArrayClass(String className, EntryType initiatingEntry) throws ClassNotFoundException {
        try {
            Pair<byte[], Integer> res = classFileReader.readClassFile(className, initiatingEntry);
            //data里面储存的是二进制的class文件
            byte[] data = res.getKey();
            //definingEntry里面存的是相应class文件的真实的类加载器
            EntryType definingEntry = new EntryType(res.getValue());
            //define class
            JClass clazz = defineClass(data, definingEntry);
            //link class
            linkClass(clazz);
            return clazz;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClassNotFoundException();
        }
    }

    /**
     *
     * define class
     * @param data binary of class file
     * @param definingEntry defining loader of class
     */
    private JClass defineClass(byte[] data, EntryType definingEntry) throws ClassNotFoundException, IOException {
        ClassFile classFile = new ClassFile(data);
        JClass clazz = new JClass(classFile);
        //todo
        /**
         * Add some codes here.
         *
         * update load entry of the class
         * load superclass recursively
         * load interfaces of this class
         * add to method area
         */
        clazz.setLoadEntryType(definingEntry);
        resolveSuperClass(clazz);
        resolveInterfaces(clazz);
        methodArea.addClass(clazz.getName(), clazz);
        return clazz;
    }

    /**
     * load superclass before add to method area
     */
    private void resolveSuperClass(JClass clazz) throws ClassNotFoundException{
        //todo
        /**
         * Add some codes here.
         *
         * Use the load entry(defining entry) as initiating entry of super class
         */
        if(!clazz.getName().equals("java/lang/Object")) {
            clazz.setSuperClass(loadClass(clazz.getSuperClassName(), clazz.getLoadEntryType()));
        }
    }

    /**
     * load interfaces before add to method area
     */
    private void resolveInterfaces(JClass clazz) throws ClassNotFoundException {
        //todo
        /**
         * Add some codes here.
         *
         * Use the load entry(defining entry) as initiating entry of interfaces
         */
        int count = 0;
        JClass [] interfaces = new JClass [clazz.getInterfaceNames().length];
        for(String interfaceName: clazz.getInterfaceNames()){
            interfaces[count] = loadClass(interfaceName, clazz.getLoadEntryType());
        }
        clazz.setInterfaces(interfaces);
    }

    /**
     * link phase
     */
    private void linkClass(JClass clazz) {
        verify(clazz);
        prepare(clazz);
    }

    /**
     * You don't need to write any code here.
     */
    private void verify(JClass clazz) {
        //do nothing
    }

    private void prepare(JClass clazz) {
        //todo
        /**
         * Add some codes here.
         *
         * step1 (We do it for you here)
         *      count the fields slot id in instance        实例变量
         *      count the fields slot id in class           静态变量
         * step2
         *      alloc memory for fields(We do it for you here) and init static vars
         * step3
         *      set the init state
         */
        calInstanceFieldSlotIDs(clazz);
        calStaticFieldSlotIDs(clazz);
        allocAndInitStaticVars(clazz);
        clazz.setInitState(InitState.PREPARED);
    }

    /**
     * count the number of field slots in instance
     * long and double takes two slots
     * the field is not static
     */
    //父类成员变量加上子类自己的成员变量
    private void calInstanceFieldSlotIDs(JClass clazz) {
        int slotID = 0;
        if (clazz.getSuperClass() != null) {
            slotID = clazz.getSuperClass().getInstanceSlotCount();
        }
        Field[] fields = clazz.getFields();
        for (Field f : fields) {
            if (!f.isStatic()) {
                f.setSlotID(slotID);
                slotID++;
                if (f.isLongOrDouble()) slotID++;
            }
        }
        clazz.setInstanceSlotCount(slotID);
    }

    /**
     * count the number of field slots in class
     * long and double takes two slots
     * the field is static
     */
    private void calStaticFieldSlotIDs(JClass clazz) {
        int slotID = 0;
        Field[] fields = clazz.getFields();
        for (Field f : fields) {
            if (f.isStatic()) {
                //为每一个field设置唯一对应的soltID，用于对StaticVars的初始化
                f.setSlotID(slotID);
                slotID++;
                if (f.isLongOrDouble()) slotID++;
            }
        }
        clazz.setStaticSlotCount(slotID);

    }

    /**
     * primitive type is set to 0
     * ref type is set to null
     */
    private void initDefaultValue(JClass clazz, Field field) {
        //todo
        /**
         * Add some codes here.
         * step 1
         *      get static vars of class
         * step 2
         *      get the slotID index of field
         * step 3
         *      switch by descriptor or some part of descriptor
         *      Handle basic type ZBCSIJDF and references (with new NullObject())
         */
        Vars staticVars = clazz.getStaticVars();
        int soltID = field.getSlotID();
        switch(field.descriptor) {
            case("Z"):
            case("B"):
            case("S"):
            case("I"):
            case("C"):
                staticVars.setInt(soltID, 0);
                break;
            case("F"):
                staticVars.setFloat(soltID, 0);
                break;
            case("D"):
                staticVars.setDouble(soltID, 0);
                break;
            case("L"):
                staticVars.setLong(soltID, 0);
                break;
            default:
                staticVars.setObjectRef(soltID, new NullObject());
                break;
        }
    }

    /**
     * load const value from runtimeConstantPool for primitive type
     * String is not support now
     */
    private void loadValueFromRTCP(JClass clazz, Field field) {
        //todo
        /**
         * Add some codes here.
         *
         * step 1
         *      get static vars and runtime constant pool of class
         * step 2
         *      get the slotID and constantValue index of field
         * step 3
         *      switch by descriptor or some part of descriptor
         *      just handle basic type ZBCSIJDF, you don't have to throw any exception
         *      use wrapper to get value
         *
         *  Example
         *      long longVal = ((LongWrapper) runtimeConstantPool.getConstant(constantPoolIndex)).getValue();
         */
        Vars staticVars = clazz.getStaticVars();
        RuntimeConstantPool runtimeConstantPool = clazz.getRuntimeConstantPool();
        int slotID = field.getSlotID();
        int constValueIndex = field.getConstValueIndex();
        switch(field.descriptor){
            case("Z"):
            case("B"):
            case("C"):
            case("S"):
            case("I"):
                int val = ((IntWrapper) runtimeConstantPool.getConstant(constValueIndex)).getValue();
                staticVars.setInt(slotID, val);
                break;
            case("F"):
                float floatVal = ((FloatWrapper) runtimeConstantPool.getConstant(constValueIndex)).getValue();
                staticVars.setFloat(slotID, floatVal);
                break;
            case("D"):
                double doubleVal = ((DoubleWrapper) runtimeConstantPool.getConstant(constValueIndex)).getValue();
                staticVars.setDouble(slotID, doubleVal);
                break;
            case("J"):
                long longVal = ((LongWrapper) runtimeConstantPool.getConstant(constValueIndex)).getValue();
                staticVars.setLong(slotID, longVal);
                break;
            default:
                break;
        }
    }

    /**
     * the value of static final field is in com.njuse.seecjvm.runtime constant pool
     * others will be set to default value
     */
    private void allocAndInitStaticVars(JClass clazz) {
        //init StaticVars
        clazz.setStaticVars(new Vars(clazz.getStaticSlotCount()));
        Field[] fields = clazz.getFields();
        //todo
        /**
         * Add some codes here.
         *
         * Refer to manual for details.
         */
        for (Field f : fields) {
            if(f.isStatic() && f.isFinal())
                loadValueFromRTCP(clazz, f);
            else
                initDefaultValue(clazz, f);
        }
    }
}
