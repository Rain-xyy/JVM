package edu.nju;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * format : dir/subdir/target.jar
 */
public class ArchivedEntry extends Entry {
    public ArchivedEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException{
        File file = new File(this.classpath);
        //String path = file.getAbsolutePath();
        ZipFile zipFile = new ZipFile(file);

        Enumeration<? extends ZipEntry> ee = zipFile.entries();
        while(ee.hasMoreElements()){
            ZipEntry entry = ee.nextElement();
            if(IOUtil.transform(entry.getName()).equals(className)){
                InputStream is = zipFile.getInputStream(entry);
                return IOUtil.readFileByBytes(is);
            }
        }
        return null;
    }
}
