package live.baize.server.mapper.disk;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import live.baize.server.bean.business.disk.UserFile;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserFileMapper extends BaseMapper<UserFile> {

    @Select("select realPath, fileSize from UserFile where UID = #{UID} and fileState = 'N' and deleteTime < DATE_SUB(CURRENT_TIMESTAMP, INTERVAL #{days} day);")
    List<UserFile> selectFileListOutOfDate(@Param("UID") int UId, @Param("days") int days);

    @Delete("delete from UserFile where UID = #{UID} and fileState = 'N' and deleteTime < DATE_SUB(CURRENT_TIMESTAMP, INTERVAL #{days} day);")
    Integer clearFileListOutOfDate(@Param("UID") int UId, @Param("days") int days);

}
