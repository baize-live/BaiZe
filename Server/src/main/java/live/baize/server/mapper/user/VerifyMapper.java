package live.baize.server.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import live.baize.server.bean.business.Verify;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface VerifyMapper extends BaseMapper<Verify> {
}
