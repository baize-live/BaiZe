package top.byze.utils;

import java.io.File;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;


public class FileUtil {

    // 创建目录
    public static void createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // 保存文件
    public static void saveFile(FileItem fileItem, String path) {
        try {
            fileItem.write(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 删除文件
    public static void deleteFile(String path) {
        try {
            FileUtils.delete(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
