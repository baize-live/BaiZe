package top.byze.utils;

import java.io.File;

import org.apache.commons.fileupload.FileItem;


public class FileUtil {

    public static void createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }


    public static void saveFile(FileItem fileItem, String path) {
        try {
            fileItem.write(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
