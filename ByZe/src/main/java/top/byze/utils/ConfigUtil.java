package top.byze.utils;

import java.util.Properties;

/**
 * @author CodeXS
 */
public class ConfigUtil {
    /**
     * 拿到 UserFilePath
     */
    public static String getUserFilePath() {
        Properties config = new Properties();
        try {
            config.load(ConfigUtil.class.getClassLoader().getResourceAsStream("filePath.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config.getProperty("UserFilePath");
    }

    /**
     * 拿到 ShareFilePath
     */
    public static String getShareFilePath() {
        Properties config = new Properties();
        try {
            config.load(ConfigUtil.class.getClassLoader().getResourceAsStream("filePath.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config.getProperty("ShareFilePath");
    }
}
