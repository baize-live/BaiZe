package live.baize.server.bean.business.disk;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author CodeXS
 */
@Data
@Accessors(chain = true)
public class UserFile {
    private Integer uid;
    private String fileName;
    private String fileDir;
    private Integer ufId;
    private String fileType;
    private Integer fileSize;
    private String fileState;
    private String deleteTime;

    public UserFile() {
    }

    // 唯一标识一个文件
    public UserFile(Integer uid, String fileName, String fileDir) {
        this.uid = uid;
        this.fileName = fileName;
        this.fileDir = fileDir;
    }
}
