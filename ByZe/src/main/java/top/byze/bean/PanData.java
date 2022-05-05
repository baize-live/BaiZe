package top.byze.bean;

public class PanData {
    private String pid;
    private String uid;
    private String grade;
    private long nowStorage;
    private long maxStorage;
    private String createTime;

    public String getGrade() {
        return grade;
    }

    public String getUid() {
        return uid;
    }

    public long getNowStorage() {
        return nowStorage;
    }

    public long getMaxStorage() {
        return maxStorage;
    }
}
