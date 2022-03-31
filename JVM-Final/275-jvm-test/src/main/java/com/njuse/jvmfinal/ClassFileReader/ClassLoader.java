package com.njuse.jvmfinal.ClassFileReader;

import com.njuse.jvmfinal.ClassFileReader.classpath.EntryType;
import com.njuse.jvmfinal.classloader.classfileparser.ClassFile;
import com.njuse.jvmfinal.memory.jclass.Field;
import com.njuse.jvmfinal.memory.jclass.InitState;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.MethodArea;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.DoubleWrapper;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.FloatWrapper;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.IntWrapper;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.LongWrapper;
import com.njuse.jvmfinal.runtime.NullObject;
import com.njuse.jvmfinal.runtime.Vars;
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

    public JClass loadClass(String className, EntryType initiatingEntry) throws ClassNotFoundException {
        JClass ret;
        //判断有没有被加载过，如果已经被加载过，直接返回ret，否则就加载对应的类
        if ((ret = this.methodArea.findClass(className)) == null) {
            return className.charAt(0) == '[' ? this.loadArrayClass(className, initiatingEntry) : this.loadNonArrayClass(className, initiatingEntry);
        } else {
            return ret;
        }
    }

    private JClass loadNonArrayClass(String className, EntryType initiatingEntry) throws ClassNotFoundException {
        try {
            Pair<byte[], Integer> res = classFileReader.readClassFile(className, initiatingEntry);
            //data里面储存的是二进制的class文件
            byte[] data = res.getKey();
            //definingEntry里面存的是相应class文件的初始类加载器
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

    private JClass loadArrayClass(String className, EntryType initiatingEntry) throws ClassNotFoundException {
        JClass clazz = new JClass();
        clazz.setName(className);
        clazz.setLoadEntryType(initiatingEntry);
        clazz.setAccessFlags((short)1);
        clazz.setSuperClass(this.tryLoad("java/lang/Object", initiatingEntry));
        JClass[] interfaces = new JClass[]{this.tryLoad("java/lang/Cloneable", initiatingEntry), this.tryLoad("java/io/Serializable", initiatingEntry)};
        clazz.setInterfaces(interfaces);
        clazz.setInitState(InitState.SUCCESS);
        this.methodArea.addClass(className, clazz);
        return clazz;
    }

    private JClass tryLoad(String className, EntryType initiatingEntry) throws ClassNotFoundException {
        JClass ret;
        return (ret = this.methodArea.findClass(className)) == null ? this.loadNonArrayClass(className, initiatingEntry) : ret;
    }


    private JClass defineClass(byte[] data, EntryType definingEntry) throws ClassNotFoundException, IOException {
        ClassFile classFile = new ClassFile(data);
        JClass clazz = new JClass(classFile);
        clazz.setLoadEntryType(definingEntry);
        resolveSuperClass(clazz);
        resolveInterfaces(clazz);
        methodArea.addClass(clazz.getName(), clazz);
        return clazz;
    }


    private void resolveSuperClass(JClass clazz) throws ClassNotFoundException{
        if(!clazz.getName().equals("java/lang/Object")) {
            clazz.setSuperClass(loadClass(clazz.getSuperClassName(), clazz.getLoadEntryType()));
        }
    }


    private void resolveInterfaces(JClass clazz) throws ClassNotFoundException {
        int count = 0;
        JClass [] interfaces = new JClass [clazz.getInterfaceNames().length];
        for(String interfaceName: clazz.getInterfaceNames()){
            interfaces[count] = loadClass(interfaceName, clazz.getLoadEntryType());
        }
        clazz.setInterfaces(interfaces);
    }


    private void linkClass(JClass clazz) {
        verify(clazz);
        prepare(clazz);
    }


    private void verify(JClass clazz) {
        //do nothing
    }

    private void prepare(JClass clazz) {
        calInstanceFieldSlotIDs(clazz);
        calStaticFieldSlotIDs(clazz);
        allocAndInitStaticVars(clazz);
        clazz.setInitState(InitState.PREPARED);
    }


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


    private void initDefaultValue(JClass clazz, Field field) {
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
            case("J"):
                staticVars.setLong(soltID, 0);
                break;
            default:
                staticVars.setObjectRef(soltID, new NullObject());
                break;
        }
    }


    private void loadValueFromRTCP(JClass clazz, Field field) {
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


    private void allocAndInitStaticVars(JClass clazz) {
        //init StaticVars
        clazz.setStaticVars(new Vars(clazz.getStaticSlotCount()));
        Field[] fields = clazz.getFields();
        for (Field f : fields) {
            if(f.isStatic() && f.isFinal())
                loadValueFromRTCP(clazz, f);
            else if(f.isStatic())
                initDefaultValue(clazz, f);
        }
    }
}
