package live.baize.server.bean.business.disk;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DiskData {
    private final Integer uid;
    private Integer did;
    private String icon;
    private Integer grade;
    private Integer outOfDate;
    private Integer nowStorage;
    private Integer maxStorage;
    private String createTime;

    public DiskData(Integer uid) {
        this.uid = uid;
    }
}
