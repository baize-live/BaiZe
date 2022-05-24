package top.byze.bean;

public class UserFile {
    private String UFID;
    private int UID;
    private String fileName;
    private String fileType;
    private long fileSize;
    private String fileState;
    private String fileDir;
    private String deleteTime;

    public UserFile(int UID, String fileName, String fileDir) {
        this.UID = UID;
        this.fileName = fileName;
        this.fileDir = fileDir;
    }

    public String getUFID() {
        return UFID;
    }

    public int getUID() {
        return UID;
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
                "UFID='" + UFID + '\'' +
                ", UID='" + UID + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", fileState='" + fileState + '\'' +
                ", lastDir='" + fileDir + '\'' +
                ", deleteTime='" + deleteTime + '\'' +
                '}';
    }
}
