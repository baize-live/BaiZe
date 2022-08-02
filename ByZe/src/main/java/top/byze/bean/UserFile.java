package top.byze.bean;

import lombok.Getter;

/**
 * @author CodeXS
 */
@Getter
public class UserFile {
    private Integer uid;
    private String fileName;
    private String fileDir;
    private Integer ufId;
    private String fileType;
    private Integer fileSize;
    private String fileState;
    private String deleteTime;

    // 非数据库字段 用于生成不同的sql语句
    private Integer selectConditionTypeId;
    private Integer updateConditionTypeId;
    private Integer deleteConditionTypeId;

    public UserFile() {
    }

    // 唯一标识一个文件
    public UserFile(Integer uid, String fileName, String fileDir) {
        this.uid = uid;
        this.fileName = fileName;
        this.fileDir = fileDir;
    }

    public UserFile setUid(Integer uid) {
        this.uid = uid;
        return this;
    }

    public UserFile setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public UserFile setFileDir(String fileDir) {
        this.fileDir = fileDir;
        return this;
    }

    public UserFile setFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public UserFile setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public UserFile setFileState(String fileState) {
        this.fileState = fileState;
        return this;
    }

    public UserFile setDeleteTime(String deleteTime) {
        this.deleteTime = deleteTime;
        return this;
    }

    public UserFile setSelectConditionTypeId(Integer selectConditionTypeId) {
        this.selectConditionTypeId = selectConditionTypeId;
        return this;
    }

    public UserFile setUpdateConditionTypeId(Integer updateConditionTypeId) {
        this.updateConditionTypeId = updateConditionTypeId;
        return this;
    }

    public UserFile setDeleteConditionTypeId(Integer deleteConditionTypeId) {
        this.deleteConditionTypeId = deleteConditionTypeId;
        return this;
    }
}
