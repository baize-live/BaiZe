package top.byze.utils;

import java.util.Properties;

public class ConfigUtil {

    public static String getUserFilePath() {
        Properties config = new Properties();
        try {
            config.load(ConfigUtil.class.getClassLoader().getResourceAsStream("filePath.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config.getProperty("UserFilePath");
    }

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
