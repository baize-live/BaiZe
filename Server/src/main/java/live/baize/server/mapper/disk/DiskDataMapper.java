package live.baize.server.mapper.disk;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import live.baize.server.bean.business.disk.DiskData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DiskDataMapper extends BaseMapper<DiskData> {
}
