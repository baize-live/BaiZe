package top.byze.bean;

/**
 * @author CodeXS
 */
public class UserFile {
    private final int userId;
    private final String fileName;
    private final String fileDir;
    private String userFileId;
    private String fileType;
    private long fileSize;
    private String fileState;
    private String deleteTime;

    public UserFile(int userId, String fileName, String fileDir) {
        this.userId = userId;
        this.fileName = fileName;
        this.fileDir = fileDir;
    }

    public String getUserFileId() {
        return userFileId;
    }

    public int getUserId() {
        return userId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public long getFileSize() {
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

    @Override
    public String toString() {
        return "UserFile{" +
                "UFID='" + userFileId + '\'' +
                ", UID='" + userId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", fileState='" + fileState + '\'' +
                ", lastDir='" + fileDir + '\'' +
                ", deleteTime='" + deleteTime + '\'' +
                '}';
    }
}
