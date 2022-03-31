package edu.nju;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * format : dir/subdir/.../
 */
public class DirEntry extends Entry {
    public DirEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        String path = this.classpath + FILE_SEPARATOR + className;
        try {
            InputStream is = new FileInputStream(path);
            return IOUtil.readFileByBytes(is);
        }catch(FileNotFoundException e){
            return null;
        }
    }
}

