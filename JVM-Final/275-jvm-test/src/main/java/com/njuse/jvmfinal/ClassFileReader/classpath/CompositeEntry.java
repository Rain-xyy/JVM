package com.njuse.jvmfinal.ClassFileReader.classpath;

import com.njuse.jvmfinal.ClassFileReader.ClassFileReader;

import java.io.IOException;

public class CompositeEntry extends Entry{
    public CompositeEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        String [] PathArr = this.classpath.split(PATH_SEPARATOR);
        for(String path: PathArr){
            Entry entry = ClassFileReader.chooseEntryType(path);
            if(entry.readClassFile(className) != null)
                return entry.readClassFile(className);
        }
        return null;
    }
}
