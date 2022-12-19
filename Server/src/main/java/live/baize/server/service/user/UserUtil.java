package live.baize.server.service.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import live.baize.server.bean.business.User;
import live.baize.server.bean.business.disk.DiskData;
import live.baize.server.bean.exception.BusinessException;
import live.baize.server.bean.exception.SystemException;
import live.baize.server.bean.response.ResponseEnum;
import live.baize.server.mapper.disk.DiskDataMapper;
import live.baize.server.mapper.user.UserMapper;
import live.baize.server.service.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service
@PropertySource("classpath:config.properties")
public class UserUtil {
    @Value("${filepath.user}")
    String userFilePath;
    @Value("${filepath.share}")
    String shareFilePath;

    @Resource
    private UserMapper userMapper;
    @Resource
    private DiskDataMapper diskDataMapper;

    /**
     * 检查邮箱是否已经注册
     */
    public boolean checkEmail(String email) {
        return userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("email", email)
                        .select("UID")
        ) != null;
    }

    /**
     * 添加用户
     */
    public boolean addUser(String username, String password, String email) {
        return userMapper.insert(
                new User(email).setUsername(username).setPassword(password)
        ) == 1;
    }

    /**
     * 删除用户
     */
    public boolean delUser(String email) {
        // TODO: 删除用户
//          需要考虑
//          1. 先删除所有业务表中的数据
//          2. 再删除用户表
        return userMapper.delete(
                new QueryWrapper<User>()
                        .eq("email", email)
        ) == 1;
    }

    /**
     * 查找用户
     */
    public boolean findUser(String email, String password) {
        return userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("email", email)
                        .eq("password", password)
                        .select("UID")
        ) != null;
    }

    /**
     * 是否开通网盘
     */
    public boolean isOpenDisk(String email) {
        return "1".equals(userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("email", email)
                        .select("isOpenDisk")
        ).getIsOpenDisk());
    }

    /**
     * 开通网盘
     */
    @Transactional(rollbackFor = SystemException.class)
    public boolean openDisk(String email) {
        // 检验是否已经开通
        if (isOpenDisk(email)) {
            throw new BusinessException(ResponseEnum.Has_OpenDisk);
        }

        int ret;
        // 1. 修改user表中用户的isOpenDisk字段
        ret = userMapper.update(null,
                new UpdateWrapper<User>()
                        .eq("email", email)
                        .set("isOpenDisk", "1")
        );
        if (ret == 0) {
            log.info("modify isOpenDisk failure.");
            return false;
        }

        // 2. 拿到 UID
        Integer UId = userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("email", email)
                        .select("UID")
        ).getUid();

        // 3. 在DiskData表中添加用户信息
        ret = diskDataMapper.insert(new DiskData(UId));
        if (ret == 0) {
            log.info("insert DiskData failure.");
            throw new SystemException(ResponseEnum.SYSTEM_UNKNOWN);
        }

        // 4. 创建的文件目录
        String path = userFilePath + "User" + UId;
        if (!FileUtil.createDir(path)) {
            log.info("createDir failure.");
            throw new SystemException(ResponseEnum.SYSTEM_UNKNOWN);
        }

        return true;
    }

    /**
     * 是否开通游戏
     */
    public boolean isOpenGame(String email) {
        return "1".equals(userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("email", email)
                        .select("isOpenGame")
        ).getIsOpenGame());
    }

    /**
     * 开通游戏
     */
    public boolean openGame(String email) {
        return false;
    }

}
