package edu.nju;

import java.io.IOException;

/**
 * format : dir/subdir;dir/subdir/*;dir/target.jar*
 */
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
        /*DirEntry dirEntry = new DirEntry(strArr[0]);
        ArchivedEntry archivedEntry = new ArchivedEntry(strArr[1]);
        WildEntry wildEntry = new WildEntry(strArr[2]);
        if(dirEntry.readClassFile(className) != null)
            return dirEntry.readClassFile(className);
        else if(archivedEntry.readClassFile(className) != null)
            return archivedEntry.readClassFile(className);
        else
            return wildEntry.readClassFile(className);*/
    }
}
