package com.njuse.jvmfinal.ClassFileReader.classpath;

import com.njuse.jvmfinal.util.IOUtil;
import com.njuse.jvmfinal.util.PathUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class WildEntry extends Entry{

    public WildEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        File file = new File(this.classpath.replace(FILE_SEPARATOR + "*", ""));
        File[] jars = file.listFiles();
        if (jars != null && jars.length != 0) {
            for (File file1 : jars) {
                String fPath = file1.getAbsolutePath();
                if (fPath.endsWith(".jar") || fPath.endsWith(".JAR")) {
                    JarFile jarFile = new JarFile(file1);
                    Enumeration<JarEntry> ee = jarFile.entries();
                    while(ee.hasMoreElements()){
                        JarEntry entry = ee.nextElement();
                        if(entry.getName().equals(className)) {
                            InputStream is = jarFile.getInputStream(entry);
                            return IOUtil.readFileByBytes(is);
                        }
                    }
                }
            }
        }
        return null;
    }
}
