package top.byze.mapper;

import org.apache.ibatis.annotations.Param;
import top.byze.bean.PanData;

public interface PanDataMapper {
    void initData(@Param("UID") int uid);

    PanData getPanData(@Param("UID") int uid);
}
