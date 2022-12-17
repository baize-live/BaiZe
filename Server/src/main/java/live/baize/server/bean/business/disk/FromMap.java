package live.baize.server.bean.business.disk;

import org.apache.commons.fileupload.FileItem;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CodeXS
 */
public class FromMap {

    private final Map<String, String> paramMap;
    private final Map<String, FileItem> fileMap;

    public FromMap() {
        paramMap = new HashMap<>();
        fileMap = new HashMap<>();
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public Map<String, FileItem> getFileMap() {
        return fileMap;
    }

}

