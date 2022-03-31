package com.njuse.jvmfinal.memory.jclass;


import java.util.LinkedHashMap;
import java.util.Map;

public class MethodArea {
    private static MethodArea methodArea = new MethodArea();
    private static Map<String, JClass> classMap;
    private static Map<String, Boolean> classState;

    private MethodArea() {
        classMap = new LinkedHashMap();
        classState = new LinkedHashMap();
    }

    public static MethodArea getInstance() {
        return methodArea;
    }

    public JClass findClass(String className) {
        return classMap.keySet().stream().anyMatch((name) -> {
            return name.equals(className);
        }) ? classMap.get(className) : null;
    }

    public void addClass(String className, JClass clazz) {
        classMap.put(className, clazz);
        classState.put(className, true);
    }

    public static Map<String, JClass> getClassMap() {
        return classMap;
    }

    public static Map<String, Boolean> getClassState() {
        return classState;
    }
}
