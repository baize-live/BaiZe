package live.baize.server.service.disk;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import live.baize.server.bean.business.disk.DiskData;
import live.baize.server.bean.business.disk.UserFile;
import live.baize.server.bean.exception.BusinessException;
import live.baize.server.bean.exception.SystemException;
import live.baize.server.bean.response.ResponseEnum;
import live.baize.server.mapper.disk.DiskDataMapper;
import live.baize.server.mapper.disk.UserFileMapper;
import live.baize.server.service.utils.PasswdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


@Slf4j
@Service
public class DiskUtil {
    @Value("${filepath.user}")
    private String userFilePath;

    @Resource
    private DiskDataMapper diskDataMapper;
    @Resource
    private UserFileMapper userFileMapper;

    @Resource
    private PasswdUtil passwdUtil;

    public DiskData getDiskDataByUId(Integer UId) {
        return diskDataMapper.selectOne(
                new QueryWrapper<DiskData>()
                        .eq("UID", UId)
        );
    }

    public List<UserFile> getUserFileAtDir(Integer UId, String fileDir) {
        return userFileMapper.selectList(
                new QueryWrapper<UserFile>()
                        .eq("UID", UId)
                        .eq("fileDir", fileDir)
                        .eq("fileState", "Y") // 表示未被删除
                        .select("fileName", "fileType", "fileSize")
        );
    }

    @Transactional(rollbackFor = SystemException.class)
    public boolean saveUserFile(Integer UId, String fileDir, String fileName, MultipartFile file) {
        // 文件大小
        Long fileSize = file.getSize();
        // 生成40位真实路径
        String realPath = passwdUtil.generateFileName(UId, fileDir, fileName);

        // 1. 插入 UserFile表
        try {
            if (userFileMapper.insert(new UserFile(UId, fileDir, fileName).setRealPath(realPath).setFileSize(fileSize)) != 1) {
                return false;
            }
        } catch (Exception e) {
            throw new BusinessException(ResponseEnum.File_Has_Exist, e.getCause());
        }

        // 2. 更新 DiskData表 TODO: 容易发生脏读
        DiskData diskData = getDiskDataByUId(UId);
        if (diskData.getNowStorage() + fileSize > diskData.getMaxStorage()) {
            throw new BusinessException(ResponseEnum.StorageNotEnough, null);
        } else {
            if (diskDataMapper.update(null, new UpdateWrapper<DiskData>().eq("UID", UId).set("nowStorage", diskData.getNowStorage() + fileSize)) != 1) {
                return false;
            }
        }

        // 3. 拿到用户home文件夹
        File folder = new File(userFilePath + UId);
        if (!folder.isDirectory() && !folder.mkdirs()) {
            throw new SystemException(ResponseEnum.CreateFileDir_Failure, null);
        }

        // 4. 保存文件
        try {
            file.transferTo(new File(folder, realPath));
        } catch (IOException e) {
            throw new SystemException(ResponseEnum.WriteFile_Failure, e.getCause());
        }

        return true;
    }

    public boolean downloadFile(Integer UId, String fileDir, String fileName, HttpServletResponse response) {
        // 1. 查询数据库, 拿到真实路径
        UserFile userFile = userFileMapper.selectOne(
                new QueryWrapper<UserFile>()
                        .eq("UID", UId)
                        .eq("fileDir", fileDir)
                        .eq("fileName", fileName)
                        .select("realPath")
        );
        if (userFile == null) {
            throw new BusinessException(ResponseEnum.Not_ThisFile, null);
        }
        String realPath = userFile.getRealPath();

        // 2. 得到要下载的文件
        File file = new File(userFilePath + UId + "/" + realPath);
        // 3. 如果文件不存在, 则显示下载失败
        if (!file.exists()) {
            throw new BusinessException(ResponseEnum.Not_ThisFile, null);
        } else {
            // 设置相应头，控制浏览器下载该文件，这里就是会出现当你点击下载后，出现的下载地址框
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Length", "" + file.length());
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ";filename*=UTF-8");
            try {
                OutputStream os = response.getOutputStream();
                FileUtils.copyFile(file, os);
                os.flush();
                os.close();
            } catch (IOException e) {
                return false;
            }
        }

        return true;
    }

    public boolean deleteUserFile(Integer UId, String fileName, String fileDir) {
        // 设置UserFile中文件状态为N 表示删除 所以此时不释放空间
        return userFileMapper.update(null,
                new UpdateWrapper<UserFile>()
                        .eq("UID", UId)
                        .eq("fileDir", fileDir)
                        .eq("fileName", fileName)
                        .eq("fileState", "Y")
                        .set("fileState", "N"))
                == 1;
    }

    public boolean recoveryUserFileFromBin(Integer UId, String fileName, String fileDir) {
        // 设置UserFile中文件状态为N 表示删除 所以此时不释放空间
        return userFileMapper.update(null,
                new UpdateWrapper<UserFile>()
                        .eq("UID", UId)
                        .eq("fileDir", fileDir)
                        .eq("fileName", fileName)
                        .eq("fileState", "N")
                        .set("fileState", "Y"))
                == 1;
    }

    @Transactional(rollbackFor = SystemException.class)
    public boolean clearUserFileFromBin(Integer UId, String fileName, String fileDir) {
        DiskData diskData = diskDataMapper.selectOne(
                new QueryWrapper<DiskData>().eq("UID", UId)
                        .select("nowStorage"));

        // 查找该文件
        UserFile userFile = userFileMapper.selectOne(
                new QueryWrapper<UserFile>()
                        .eq("UID", UId)
                        .eq("fileDir", fileDir)
                        .eq("fileName", fileName)
                        .eq("fileState", "N")
                        .select("fileSize", "realPath")
        );

        if (userFile == null) {
            return false;
        }

        // 在磁盘上删除
        try {
            FileUtils.delete(new File(userFilePath + UId + "/" + userFile.getRealPath()));
        } catch (IOException ioException) {
            throw new SystemException(ResponseEnum.SYSTEM_UNKNOWN, ioException.getCause());
        }

        // 数据库删除
        if (userFileMapper.delete(new QueryWrapper<UserFile>()
                .eq("UID", UId).eq("fileDir", fileDir)
                .eq("fileName", fileName).eq("fileState", "N")) != 1) {
            throw new SystemException(ResponseEnum.SYSTEM_UNKNOWN);
        }

        // 更新当前存储
        if (diskDataMapper.update(null, new UpdateWrapper<DiskData>().eq("UID", UId)
                .set("nowStorage", diskData.getNowStorage() - userFile.getFileSize())) != 1) {
            throw new SystemException(ResponseEnum.SYSTEM_UNKNOWN);
        }

        return true;
    }

    @Transactional(rollbackFor = SystemException.class)
    public boolean clearAllFilesFromBin(Integer UId) {
        DiskData diskData = diskDataMapper.selectOne(
                new QueryWrapper<DiskData>().eq("UID", UId)
                        .select("nowStorage"));
        List<UserFile> fileList = userFileMapper.selectList(
                new QueryWrapper<UserFile>()
                        .eq("UID", UId)
                        .eq("fileState", "N")
                        .select("fileSize", "realPath")
        );

        if (fileList.size() == 0) {
            return true;
        }

        Long allFileSize = 0L;

        // 删除磁盘中的文件
        try {
            for (UserFile file : fileList) {
                allFileSize += file.getFileSize();
                FileUtils.delete(new File(userFilePath + UId + "/" + file.getRealPath()));
            }
        } catch (IOException ioException) {
            throw new SystemException(ResponseEnum.SYSTEM_UNKNOWN, ioException.getCause());
        }

        // 删除数据库中的记录
        if (userFileMapper.delete(new QueryWrapper<UserFile>()
                .eq("UID", UId).eq("fileState", "N")) != fileList.size()) {
            throw new SystemException(ResponseEnum.SYSTEM_UNKNOWN);
        }

        // 更新当前存储
        if (diskDataMapper.update(null, new UpdateWrapper<DiskData>()
                .eq("UID", UId)
                .set("nowStorage", diskData.getNowStorage() - allFileSize)) != 1) {
            throw new SystemException(ResponseEnum.SYSTEM_UNKNOWN);
        }
        return true;
    }

    @Transactional(rollbackFor = SystemException.class)
    public List<UserFile> lookupAllFilesFromBin(Integer UId) {
        DiskData diskData = diskDataMapper.selectOne(
                new QueryWrapper<DiskData>().eq("UID", UId)
                        .select("outOfDate", "nowStorage"));
        // 拿到用户自定义的过期时间
        Integer outOfDate = diskData.getOutOfDate();

        // 查看垃圾箱前 删除过期文件
        List<UserFile> fileListOutOfDate = userFileMapper.selectFileListOutOfDate(UId, outOfDate);

        // 存在过期文件
        if (fileListOutOfDate.size() > 0) {
            // 过期文件的总大小
            Long allFileSize = 0L;

            // 删除磁盘中的过期文件
            try {
                for (UserFile file : fileListOutOfDate) {
                    allFileSize += file.getFileSize();
                    FileUtils.delete(new File(userFilePath + UId + "/" + file.getRealPath()));
                }
            } catch (IOException ioException) {
                throw new SystemException(ResponseEnum.SYSTEM_UNKNOWN, ioException.getCause());
            }

            // 删除数据库中的过期文件
            if (userFileMapper.clearFileListOutOfDate(UId, outOfDate) != fileListOutOfDate.size()) {
                throw new SystemException(ResponseEnum.SYSTEM_UNKNOWN);
            }

            // 更新当前存储
            if (diskDataMapper.update(null, new UpdateWrapper<DiskData>().eq("UID", UId)
                    .set("nowStorage", diskData.getNowStorage() - allFileSize)) != 1) {
                throw new SystemException(ResponseEnum.SYSTEM_UNKNOWN);
            }
        }

        // 查看垃圾箱
        return userFileMapper.selectList(
                new QueryWrapper<UserFile>()
                        .eq("UID", UId)
                        .eq("fileState", "N")
                        .select("fileName", "fileType", "fileSize", "deleteTime")
        );
    }
}
