package live.baize.server.mapper.disk;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import live.baize.server.bean.business.disk.UserFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserFileMapper extends BaseMapper<UserFile> {
}
