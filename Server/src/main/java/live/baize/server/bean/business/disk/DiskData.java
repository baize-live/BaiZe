package live.baize.server.bean.business.disk;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("DiskData")
public class DiskData {
    private Integer UId;
    private Integer DId;
    private Integer grade;
    private Integer outOfDate;
    private Long nowStorage;
    private Long maxStorage;
    private Date createTime;

    public DiskData(Integer UId) {
        this.UId = UId;
    }
}
