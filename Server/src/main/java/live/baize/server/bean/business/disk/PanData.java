package live.baize.server.bean.business.disk;

import lombok.Getter;

/**
 * @author CodeXS
 */
@Getter
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

    public PanData setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public PanData setGrade(Integer grade) {
        this.grade = grade;
        return this;
    }

    public PanData setOutOfDate(Integer outOfDate) {
        this.outOfDate = outOfDate;
        return this;
    }

    public PanData setNowStorage(Integer nowStorage) {
        this.nowStorage = nowStorage;
        return this;
    }

    public PanData setMaxStorage(Integer maxStorage) {
        this.maxStorage = maxStorage;
        return this;
    }
}
