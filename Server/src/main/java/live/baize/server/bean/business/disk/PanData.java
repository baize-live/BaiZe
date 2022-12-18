package live.baize.server.bean.business.disk;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
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
}
