package top.byze.mapper;

import org.apache.ibatis.annotations.Param;
import top.byze.bean.UserFile;

import java.util.List;

public interface UserFileMapper {

    List<UserFile> getFileList(@Param("UID") int uid, @Param("fileDir") String fileDir);

    void saveUserFile(@Param("UID") int uid, @Param("fileName") String fileName, @Param("fileType") char fileType, @Param("fileSize") long fileSize, @Param("fileState") char fileState, @Param("fileDir") String fileDir);

    void deleteUserFile(@Param("UID") int uid, @Param("fileName") String fileName, @Param("fileDir") String fileDir);

    void clearFilesOutOFfDateInDB(@Param("UID") int uid, @Param("days") int days);

    List<UserFile> selectFilesOutOFfDateInDB(@Param("UID") int uid, @Param("days") int days);

    List<UserFile> lookupBin(@Param("UID") int uid);

    void clearBin(@Param("UID") int uid);

    void clearUserFile(@Param("UID") int uid, @Param("fileName") String fileName, @Param("fileDir") String fileDir);

}
