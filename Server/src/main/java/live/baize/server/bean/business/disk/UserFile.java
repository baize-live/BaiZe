package live.baize.server.bean.business.disk;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class UserFile {
    private Integer UId;
    private Integer UFId;
    private String fileDir;
    private String fileName;
    private String realPath;
    private String fileType;
    private String fileState;
    private Long fileSize;
    private Date deleteTime;

    public UserFile() {
    }

    // 唯一标识一个文件
    public UserFile(Integer UId, String fileDir, String fileName) {
        this.UId = UId;
        this.fileDir = fileDir;
        this.fileName = fileName;
    }
}
