package top.byze.mapper;

import org.apache.ibatis.annotations.Param;
import top.byze.bean.PanData;

/**
 * @author CodeXS
 */

public interface PanDataMapper {

    /**
     * 获得初始数据
     *
     * @param uid 用户id
     */
    void initData(@Param("UID") int uid);

    /**
     * 拿到用户的网盘数据
     *
     * @param uid 用户id
     * @return 用户所有的网盘数据
     */
    PanData getPanData(@Param("UID") int uid);

    /**
     * 设置当前存储
     *
     * @param uid        用户id
     * @param nowStorage 当前存储
     */
    void setNowStorage(@Param("UID") int uid, @Param("nowStorage") long nowStorage);

}
