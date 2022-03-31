package com.njuse.jvmfinal.memory.jclass;

import com.njuse.jvmfinal.ClassFileReader.classpath.EntryType;
import com.njuse.jvmfinal.runtime.*;
import com.njuse.jvmfinal.classloader.classfileparser.ClassFile;
import com.njuse.jvmfinal.classloader.classfileparser.FieldInfo;
import com.njuse.jvmfinal.classloader.classfileparser.MethodInfo;
import com.njuse.jvmfinal.classloader.classfileparser.constantpool.ConstantPool;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.jvmfinal.runtime.array.*;
import com.njuse.jvmfinal.ClassFileReader.ClassLoader;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Optional;

@Getter
@Setter
public class JClass {
    private short accessFlags;
    private String name;
    private String superClassName;
    private String[] interfaceNames;
    private RuntimeConstantPool runtimeConstantPool;
    private Field[] fields;
    private Method[] methods;
    private EntryType loadEntryType; //请自行设计是否记录、如何记录加载器
    private JClass superClass;
    private JClass[] interfaces;
    private int instanceSlotCount;
    private int staticSlotCount;
    private Vars staticVars; // 请自行设计数据结构
    private InitState initState; // 请自行设计初始化状态

    public JClass(ClassFile classFile) {
        this.accessFlags = classFile.getAccessFlags();
        this.name = classFile.getClassName();
        if (!this.name.equals("java/lang/Object")) {
            // index of super class of java/lang/Object is 0
            this.superClassName = classFile.getSuperClassName();
        } else {
            this.superClassName = "";
        }
        this.interfaceNames = classFile.getInterfaceNames();
        this.fields = parseFields(classFile.getFields());
        this.methods = parseMethods(classFile.getMethods());
        this.runtimeConstantPool = parseRuntimeConstantPool(classFile.getConstantPool());
    }

    /**Todo*/
    //used for new array class!!!
    public JClass() {

    }

    public Optional<Method> resolveMethod(String name, String descriptor) {
        for (Method m : methods) {
            if (m.getDescriptor().equals(descriptor) && m.getName().equals(name)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }

    private Field[] parseFields(FieldInfo[] info) {
        int len = info.length;
        fields = new Field[len];
        for (int i = 0; i < len; i++) {
            fields[i] = new Field(info[i], this);
        }
        return fields;
    }

    private Method[] parseMethods(MethodInfo[] info) {
        int len = info.length;
        methods = new Method[len];
        for (int i = 0; i < len; i++) {
            methods[i] = new Method(info[i], this);
        }
        return methods;
    }

    private RuntimeConstantPool parseRuntimeConstantPool(ConstantPool cp) {
        return new RuntimeConstantPool(cp, this);
    }

    //创建该类新的实例
    public NonArrayObject newObject() {
        return new NonArrayObject(this);
    }

    public ArrayObject newArrayObject(int len) {
        if (this.name.charAt(0) != '[') {
            throw new RuntimeException("This Class is not array: " + this.name);
        } else {
            switch(this.name){
                case "[Z":
                    return new BooleanArrayObject(len, this.name);
                case "[C":
                    return new CharArrayObject(len, this.name);
                case "[F":
                    return new FloatArrayObject(len, this.name);
                case "[D":
                    return new DoubleArrayObject(len, this.name);
                case "[B":
                    return new ByteArrayObject(len, this.name);
                case "[S":
                    return new ShortArrayObject(len, this.name);
                case "[I":
                    return new IntArrayObject(len, this.name);
                case "[J":
                    return new LongArrayObject(len, this.name);
                default:
                    return new RefArrayObject(len, this.name);

            }
        }
    }

    public JClass getComponentClass() {
        if (this.name.charAt(0) != '[') throw new RuntimeException("Invalid Array:" + this.name);
        ClassLoader loader = ClassLoader.getInstance();
        String componentTypeDescriptor = this.name.substring(1);
        String classToLoad = null;
        if (componentTypeDescriptor.charAt(0) == '[') {
            classToLoad = componentTypeDescriptor;
        } else if (componentTypeDescriptor.charAt(0) == 'L') {
            //remove first and last char 'L' and ';'
            classToLoad = componentTypeDescriptor.substring(1, componentTypeDescriptor.length() - 1);
        } else if (this.getPrimitiveType() != null) {
            classToLoad = this.getPrimitiveType();
        }
        try {
            return loader.loadClass(classToLoad, this.loadEntryType);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Cannot load arrayClass:" + classToLoad);
    }

    /**
     * @return null if this classname is not a primitive type
     */
    private String getPrimitiveType() {
        HashMap<String, String> primitiveType = new HashMap<>();
        primitiveType.put("void", "V");
        primitiveType.put("boolean", "Z");
        primitiveType.put("byte", "B");
        primitiveType.put("short", "S");
        primitiveType.put("char", "C");
        primitiveType.put("int", "I");
        primitiveType.put("long", "J");
        primitiveType.put("float", "F");
        primitiveType.put("double", "D");
        return primitiveType.get(this.name);
    }

    /**
     * Class Init Methods
     */

    //if in multi-thread, jclass need a initstate lock
    private void initStart(JClass clazz) {
        clazz.initState = InitState.BUSY;
    }

    private void initSucceed(JClass clazz) { clazz.initState = InitState.SUCCESS; }

    private void initFail() {
        this.initState = InitState.FAIL;
    }


    public void initClass(JThread thread, JClass clazz) {
        initStart(clazz);
        Method clinit = clazz.getClinitMethod();
        if(clinit == null){
            initSucceed(clazz);
        }else {
            //先把子类的初始化方法压入栈，再看父类
            StackFrame clinitFrame = new StackFrame(thread, clinit, clinit.getMaxStack(), clinit.getMaxLocal());
            thread.pushFrame(clinitFrame);
            initSucceed(clazz);
        }
        if(!clazz.getName().equals("java/lang/Object")){
            JClass superClazz = clazz.getSuperClass();
            if (superClazz.getInitState() == InitState.PREPARED) {
                superClazz.initClass(thread.getTopFrame().getThread(), superClazz);
            }
        }
    }



    /**
     * search method in class and its superclass
     *
     * @return
     */
    private Method getMethodInClass(String name, String descriptor, boolean isStatic) {
        JClass clazz = this;
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if (m.getDescriptor().equals(descriptor)
                    && m.getName().equals(name)
                    && m.isStatic() == isStatic) {
                return m;
            }
        }
        return null;
    }



    public Method getMainMethod() {
        return getMethodInClass("main", "([Ljava/lang/String;)V", true);
    }

    public Method getClinitMethod(){ return getMethodInClass("<clinit>", "()V", true);}

    /**
     * Get extra Info
     */

    public String getPackageName() {
        int index = name.lastIndexOf('/');
        if (index >= 0) return name.substring(0, index);
        else return "";
    }

    public boolean isPublic() {
        return 0 != (this.accessFlags & AccessFlags.ACC_PUBLIC);
    }

    public boolean isInterface() {
        return 0 != (this.accessFlags & AccessFlags.ACC_INTERFACE);
    }

    public boolean isAbstract() {
        return 0 != (this.accessFlags & AccessFlags.ACC_ABSTRACT);
    }

    public boolean isAccSuper() {
        return 0 != (this.accessFlags & AccessFlags.ACC_SUPER);
    }

    public boolean isArray() {
        return this.name.charAt(0) == '[';
    }

    public boolean isJObjectClass() {
        return this.name.equals("java/lang/Object");
    }

    public boolean isJlCloneable() {
        return this.name.equals("java/lang/Cloneable");
    }

    public boolean isJIOSerializable() {
        return this.name.equals("java/io/Serializable");
    }

    public boolean isAccessibleTo(JClass caller) {
        boolean isPublic = isPublic();
        boolean inSamePackage = this.getPackageName().equals(caller.getPackageName());
        return isPublic || inSamePackage;
    }

    public boolean isAssignableFrom(JClass other) {
        JClass T = this;
        JClass S = other;
        if(S == T) return true;
        if(S.isInterface()){
            if(!T.isInterface()){
                return T.isJObjectClass();
            }else{
                return T.isSuperInterfaceOf(S);
            }
        }else if(S.isArray()){
            if(T.isArray()){
                JClass sc = S.getComponentClass();
                JClass tc = T.getComponentClass();
                return tc.isAssignableFrom(sc);
            }else if(T.isInterface()){
                return T.isJIOSerializable() || T.isJlCloneable();
            }else{
                return T.isJObjectClass();
            }
        }else{
            if(T.isInterface()){
                return S.isImplementOf(T);
            }else if(!T.isArray()){
                return S.isSubClassOf(T);
            }
        }
        return false;
    }

    public boolean isSubClassOf(JClass otherClass) {
        JClass superClass = this.getSuperClass();
        while (superClass != null) {
            if (superClass == otherClass) return true;
            superClass = superClass.getSuperClass();
        }
        return false;
    }

    private boolean isImplementOf(JClass otherInterface) {
        for(JClass superClass = this; superClass != null; superClass = this.getSuperClass()) {
            JClass[] var3 = this.getInterfaces();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                JClass i = var3[var5];
                if (i == otherInterface || i.isSubInterfaceOf(otherInterface)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isSubInterfaceOf(JClass otherInterface) {
        JClass[] superInterfaces = this.getInterfaces();
        for (JClass i : superInterfaces) {
            if (i == otherInterface || i.isSubInterfaceOf(otherInterface)) return true;
        }
        return false;
    }

    private boolean isSuperInterfaceOf(JClass otherInterface) {
        return otherInterface.isSubInterfaceOf(this);
    }
}
