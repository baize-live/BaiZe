package top.byze.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * @author CodeXS
 */
@Slf4j
public class FileUtil {
    /**
     * 创建目录
     *
     * @param path 文件夹路径
     */
    public static void createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            boolean flag = dir.mkdirs();
            if (!flag) {
                log.error("创建文件夹异常！");
            }
        }
    }

    /**
     * 保存文件
     *
     * @param path     文件路径
     * @param fileItem 文件
     */
    public static void saveFile(FileItem fileItem, String path) {
        try {
            fileItem.write(new File(path));
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    /**
     * 删除文件
     *
     * @param path 文件路径
     */
    public static void deleteFile(String path) {
        try {
            FileUtils.delete(new File(path));
        } catch (Exception e) {
            log.error(e.toString());
        }
    }
}
