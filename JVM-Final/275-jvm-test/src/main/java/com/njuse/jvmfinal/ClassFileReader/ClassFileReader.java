package com.njuse.jvmfinal.ClassFileReader;

import com.njuse.jvmfinal.ClassFileReader.classpath.*;
import com.njuse.jvmfinal.util.PathUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;

public class ClassFileReader {
    private static ClassFileReader reader = new ClassFileReader();
    //文件名分隔符
    private static final String FILE_SEPARATOR = File.separator;
    //路径分隔符
    private static final String PATH_SEPARATOR = File.pathSeparator;

    private ClassFileReader() {
    }

    private static Entry bootClasspath = null;
    private static Entry extClasspath = null;
    private static Entry userClasspath = null;

    public static ClassFileReader getInstance() {
        return reader;
    }

    public static void setBootClasspath(String classpath) {
        bootClasspath = chooseEntryType(classpath);
    }

    public static void setExtClasspath(String classpath) {
        extClasspath = chooseEntryType(classpath);
    }

    public static void setUserClasspath(String classpath) {
        userClasspath = chooseEntryType(classpath);
    }

    public Pair<byte[], Integer> readClassFile(String className, EntryType privilege) throws IOException, ClassNotFoundException {
        String realClassName = PathUtil.transform(className);
        realClassName = realClassName + ".class";
        int x;
        if(privilege == null)
            x = 0x7;
        else
            x = privilege.getValue();
        byte []ret = bootClasspath.readClassFile(realClassName);
        int y = 0x1;
        if(ret == null && x >= 0x3){
            ret = extClasspath.readClassFile(realClassName);
            y = 0x3;
            if(ret == null && x == 0x7) {
                ret = userClasspath.readClassFile(realClassName);
                y = 0x7;
            }
        }
        if(ret == null)
            throw new ClassNotFoundException();
        return Pair.of(ret, y);
    }

    //根据classpath选择加载器
    public static Entry chooseEntryType(String classpath) {
        if(classpath.contains(PATH_SEPARATOR))
            return new CompositeEntry(classpath);
        else if(classpath.contains(".zip") || classpath.contains(".ZIP") || classpath.contains(".jar") || classpath.contains(".JAR"))
            return new ArchivedEntry(classpath);
        else if(classpath.contains("*"))
            return new WildEntry(classpath);
        else
            return new DirEntry(classpath);
    }
}
