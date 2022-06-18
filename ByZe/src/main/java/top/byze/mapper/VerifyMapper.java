package top.byze.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import top.byze.bean.Verify;

/**
 * @author CodeXS
 * <p>
 * 验证码表的映射器
 */
public interface VerifyMapper {

    /**
     * 获得验证码
     *
     * @param verify 验证码
     * @return 验证码
     */
    @SelectProvider(type = Verify.class, method = "selectVerify")
    Verify selectVerify(Verify verify);

    /**
     * 插入验证码
     *
     * @param verify 验证码
     */
    @InsertProvider(type = Verify.class, method = "insertVerify")
    void insertVerify(Verify verify);

}
