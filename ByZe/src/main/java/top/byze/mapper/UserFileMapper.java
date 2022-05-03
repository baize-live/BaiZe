package top.byze.mapper;

import org.apache.ibatis.annotations.Param;
import top.byze.bean.UserFile;

import java.util.List;

public interface UserFileMapper {

    List<UserFile> getFileList(@Param("fileDir") String fileDir);

}
