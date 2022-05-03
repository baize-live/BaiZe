package top.byze.utils;

import java.io.File;

public class FileUtil {

    public static void createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }


}
