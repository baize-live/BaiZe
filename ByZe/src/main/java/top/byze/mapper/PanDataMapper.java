package top.byze.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import top.byze.bean.PanData;

import java.util.List;

/**
 * @author CodeXS
 * <p>
 * 网盘数据的映射器
 */
public interface PanDataMapper {
    /**
     * 查询网盘数据
     */
    @SelectProvider(type = PanDataSqlProvider.class, method = "selectPanData")
    List<PanData> selectPanData(PanData panData);

    /**
     * 插入网盘数据
     */
    @InsertProvider(type = PanDataSqlProvider.class, method = "insertPanData")
    void insertPanData(PanData panData);

    /**
     * 更新网盘数据
     */
    @UpdateProvider(type = PanDataSqlProvider.class, method = "updatePanData")
    void updatePanData(PanData panData);
}
