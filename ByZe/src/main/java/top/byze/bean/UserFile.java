package top.byze.bean;

public class UserFile {
    private String UFID;
    private String UID;
    private String fileName;
    private String fileType;
    private String fileSize;
    private String fileState;
    private String fileDir;
    private String createTime;

    public String getUFID() {
        return UFID;
    }

    public String getUID() {
        return UID;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getFileState() {
        return fileState;
    }

    public String getFileDir() {
        return fileDir;
    }

    public String getCreateTime() {
        return createTime;
    }

    @Override
    public String toString() {
        return "UserFile{" +
                "UFID='" + UFID + '\'' +
                ", UID='" + UID + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", fileState='" + fileState + '\'' +
                ", lastDir='" + fileDir + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
