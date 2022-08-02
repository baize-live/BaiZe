package top.byze.mapper;

import org.apache.ibatis.jdbc.SQL;
import top.byze.bean.UserFile;

/**
 * @author CodeXS
 * UserFileMapper 中使用
 */
public class UserFileSqlProvider {
    public static final int Select_By_UID_FileDir_FileState_Y = 0;
    public static final int Select_By_UID_FileState_N = 1;
    public static final int Delete_By_UID_FileState_N = 1;

    public static String selectUserFile(UserFile userFile) {
        return new SQL() {
            {
                SELECT("*");
                FROM("userFile");
                if (userFile.getSelectConditionTypeId() != null) {
                    switch (userFile.getSelectConditionTypeId()) {
                        case Select_By_UID_FileDir_FileState_Y:
                            WHERE("uid = #{uid}");
                            WHERE("fileDir = #{fileDir}");
                            WHERE("fileState = 'Y'");
                            break;
                        case Select_By_UID_FileState_N:
                            WHERE("uid = #{uid}");
                            WHERE("fileState = 'N'");
                            break;

                        default:
                            WHERE("uid = #{uid}");
                            WHERE("fileName = #{fileName}");
                            WHERE("fileDir = #{fileDir}");
                            break;
                    }
                } else {
                    // 默认 最小查询
                    WHERE("uid = #{uid}");
                    WHERE("fileName = #{fileName}");
                    WHERE("fileDir = #{fileDir}");
                }
            }
        }.toString();
    }

    public static String updateUserFile(UserFile userFile) {
        return new SQL() {
            {
                UPDATE("user");
                if (userFile.getUid() != null) {
                    SET("uid = #{uid}");
                }
                if (userFile.getFileName() != null) {
                    SET("fileName = #{fileName}");
                }
                if (userFile.getFileDir() != null) {
                    SET("fileDir = #{fileDir}");
                }
                if (userFile.getUfId() != null) {
                    SET("ufId = #{ufId}");
                }
                if (userFile.getFileType() != null) {
                    SET("fileType = #{fileType}");
                }
                if (userFile.getFileSize() != null) {
                    SET("fileSize = #{fileSize}");
                }
                if (userFile.getFileState() != null) {
                    SET("fileState = #{fileState}");
                }
                if (userFile.getDeleteTime() != null) {
                    SET("deleteTime = #{deleteTime}");
                }
                // 条件
                if (userFile.getUpdateConditionTypeId() != null) {
                    WHERE("uid = #{uid}");
                    WHERE("fileName = #{fileName}");
                    WHERE("fileDir = #{fileDir}");
                } else {
                    // 默认更新为最小更新
                    WHERE("uid = #{uid}");
                    WHERE("fileName = #{fileName}");
                    WHERE("fileDir = #{fileDir}");
                }
            }
        }.toString();
    }

    public static String insertUserFile() {
        return new SQL() {
            {
                INSERT_INTO("userFile");
                VALUES("UID", "#{UID}");
                VALUES("fileDir", "#{fileDir}");
                VALUES("fileName", "#{fileName}");
                VALUES("fileType", "#{fileType}");
                VALUES("fileSize", "#{fileSize}");
                VALUES("fileState", "'Y'");
            }
        }.toString();
    }

    public static String deleteUserFile(UserFile userFile) {
        return new SQL() {
            {
                DELETE_FROM("userFile");
                // 条件
                if (userFile.getDeleteConditionTypeId() != null) {
                    if (userFile.getDeleteConditionTypeId() == Delete_By_UID_FileState_N) {
                        WHERE("uid = #{uid}");
                        WHERE("fileState = 'N'");
                    } else {
                        WHERE("uid = #{uid}");
                        WHERE("fileName = #{fileName}");
                        WHERE("fileDir = #{fileDir}");
                    }
                } else {
                    // 默认更新为最小更新
                    WHERE("uid = #{uid}");
                    WHERE("fileName = #{fileName}");
                    WHERE("fileDir = #{fileDir}");
                }
            }
        }.toString();
    }
}
