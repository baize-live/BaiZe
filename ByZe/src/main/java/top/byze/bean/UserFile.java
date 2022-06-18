package top.byze.bean;

/**
 * @author CodeXS
 */
public class UserFile {
    private final Integer uid;
    private final String fileName;
    private final String fileDir;
    private Integer userFileId;
    private String fileType;
    private Integer fileSize;
    private String fileState;
    private String deleteTime;

    public UserFile(Integer uid, String fileName, String fileDir) {
        this.uid = uid;
        this.fileName = fileName;
        this.fileDir = fileDir;
    }

    public Integer getUid() {
        return uid;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public String getFileState() {
        return fileState;
    }

    public String getFileDir() {
        return fileDir;
    }

    public String getDeleteTime() {
        return deleteTime;
    }

}
