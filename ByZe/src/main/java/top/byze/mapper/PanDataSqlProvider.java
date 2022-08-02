package top.byze.mapper;

import org.apache.ibatis.jdbc.SQL;
import top.byze.bean.PanData;

/**
 * @author CodeXS
 * PanDataMapper 接口中使用
 */
public class PanDataSqlProvider {
    public static String selectPanData() {
        return new SQL() {
            {
                SELECT("*");
                FROM("panData");
                WHERE("uid = #{uid}");
            }
        }.toString();
    }

    public static String insertPanData() {
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
}
