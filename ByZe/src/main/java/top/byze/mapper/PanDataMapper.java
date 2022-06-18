package top.byze.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import top.byze.bean.PanData;

/**
 * @author CodeXS
 * <p>
 * 网盘数据的映射器
 */
public interface PanDataMapper {

    /**
     * 获得网盘数据
     *
     * @param panData 网盘数据
     * @return PanData 网盘数据
     */
    @SelectProvider(type = PanData.class, method = "selectPanData")
    PanData selectPanData(PanData panData);

    /**
     * 插入网盘数据
     *
     * @param panData 网盘数据
     */
    @InsertProvider(type = PanData.class, method = "insertPanData")
    void insertPanData(PanData panData);

    /**
     * 更新网盘数据
     *
     * @param panData 网盘数据
     */
    @UpdateProvider(type = PanData.class, method = "updatePanData")
    void updatePanData(PanData panData);

}
