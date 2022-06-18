package top.byze.bean;

import org.apache.ibatis.jdbc.SQL;

/**
 * @author CodeXS
 */
public class PanData {
    private final Integer uid;
    private Integer pid;
    private String icon;
    private Integer grade;
    private Integer outOfDate;
    private Integer nowStorage;
    private Integer maxStorage;
    private String createTime;

    public PanData(Integer uid) {
        this.uid = uid;
    }

    /**
     * 以下三个函数 PanDataMapper 接口中使用
     */
    public static String selectPanData(PanData panData) {
        return new SQL() {
            {
                SELECT("*");
                FROM("panData");
                WHERE("uid = #{uid}");
            }
        }.toString();
    }

    public static String insertPanData(PanData panData) {
        return new SQL() {
            {
                INSERT_INTO("panData");
                VALUES("uid", "#{uid}");
            }
        }.toString();
    }

    public static String updatePanData(PanData panData) {
        return new SQL() {
            {
                UPDATE("panData");
                if (panData.getIcon() != null) {
                    SET("icon = #{icon}");
                }
                if (panData.getGrade() != null) {
                    SET("grade = #{grade}");
                }
                if (panData.getOutOfDate() != null) {
                    SET("outOfDate = #{outOfDate}");
                }
                if (panData.getNowStorage() != null) {
                    SET("nowStorage = #{nowStorage}");
                }
                if (panData.getMaxStorage() != null) {
                    SET("maxStorage = #{maxStorage}");
                }
                WHERE("uid = #{uid}");
            }
        }.toString();
    }

    public Integer getUid() {
        return uid;
    }

    public String getIcon() {
        return icon;
    }

    public PanData setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public Integer getGrade() {
        return grade;
    }

    public PanData setGrade(Integer grade) {
        this.grade = grade;
        return this;
    }

    public Integer getOutOfDate() {
        return outOfDate;
    }

    public PanData setOutOfDate(Integer outOfDate) {
        this.outOfDate = outOfDate;
        return this;
    }

    public Integer getNowStorage() {
        return nowStorage;
    }

    public PanData setNowStorage(Integer nowStorage) {
        this.nowStorage = nowStorage;
        return this;
    }

    public Integer getMaxStorage() {
        return maxStorage;
    }

    public PanData setMaxStorage(Integer maxStorage) {
        this.maxStorage = maxStorage;
        return this;
    }

}
