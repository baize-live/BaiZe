package top.byze.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import top.byze.bean.PanData;
import top.byze.bean.User;
import top.byze.bean.UserFile;
import top.byze.mapper.PanDataMapper;
import top.byze.mapper.UserFileMapper;
import top.byze.mapper.UserMapper;
import top.byze.utils.MyBatis;
import top.byze.utils.Var;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Slf4j
public class Disk {
    HttpServletRequest req;
    HttpServletResponse res;
    PrintWriter writer = null;

    private static class Res {
        final static String TRUE = "1";
        final static String FALSE = "0";
    }

    public Disk(HttpServletRequest req, HttpServletResponse res) {
        this.req = req;
        this.res = res;
        try {
            writer = this.res.getWriter();
        } catch (IOException e) {
            log.error("获得输出流异常");
        }
    }

    // 与数据库交互
    // 获得用户
    private static User getUser(String email) {
        User user = null;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            user = userMapper.getUser(email);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("获得用户数据异常");
        }
        return user;
    }

    private static PanData getPanData(int Uid) {
        PanData panData = null;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            PanDataMapper panDataMapper = sqlSession.getMapper(PanDataMapper.class);
            panData = panDataMapper.getPanData(Uid);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("获得网盘数据异常");
            e.printStackTrace();
        }
        return panData;
    }

    private static List<UserFile> getUserFile(int uid, String fileDir) {
        List<UserFile> fileList = null;
        fileDir = Var.UserFilePath + "User" + uid + fileDir;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserFileMapper userFileMapper = sqlSession.getMapper(UserFileMapper.class);
            fileList = userFileMapper.getFileList(fileDir);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("获得文件数据异常");
        }
        return fileList;
    }

//    //
//    @RequestMapping("fileupload")
//    @ResponseBody
//    public ResultVO<Boolean> fileupload(@RequestParam("file") MultipartFile file){
//        ResultVO<Boolean> resultVO;
//        AoaUser aoaUser = getToken.getUser(token);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());					//放入Date类型数据
//        String datestr=calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DATE);
//        if(file.isEmpty()){
//            resultVO = new ResultVO<>(ErrorCode.NULL);
//            resultVO.setData(false);
//            return resultVO;
//        }
//        String attachmentName = file.getOriginalFilename();
//        String attachmentShuffix = attachmentName.substring(attachmentName.lastIndexOf(".")+1);
//        String attachmentPath = "D:/"+datestr+"-"+aoaUser.getUserName()+"-"+UUID.randomUUID()+"."+attachmentShuffix;
//        String attachmentSize = file.getSize()+"";
//        String attachmentType = "image/"+attachmentShuffix;
//        String model = "aoa_bursement";
//        Date upload_time = new Date();
//        String userId = aoaUser.getUserId().toString();
//        AoaAttachmentList aoaAttachmentList = new AoaAttachmentList(attachmentName,attachmentPath,attachmentShuffix,attachmentSize,attachmentType,model,upload_time,userId);
//
//        File dest = new File(attachmentPath);
//        if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
//            dest.getParentFile().mkdir();
//        }
//        try {
//            file.transferTo(dest); //保存文件
//        } catch (IllegalStateException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            resultVO = new ResultVO<>(ErrorCode.NULL);
//            resultVO.setData(false);
//            return resultVO;
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            resultVO = new ResultVO<>(ErrorCode.NULL);
//            resultVO.setData(false);
//            return resultVO;
//        }
//        aoaAttachmentList = aoaAttachmentListService.insertAtt(aoaAttachmentList);
//        Long proFileId = aoaAttachmentList.getAttachmentId();
//
//        if (aoaAttachmentList.getAttachmentId()!=0 ){
//            resultVO = new ResultVO<>(ErrorCode.SUCCESS);
//            resultVO.setData(true);
//            return resultVO;
//        }else {
//            resultVO = new ResultVO<>(ErrorCode.NULL);
//            resultVO.setData(false);
//            return resultVO;
//        }
//
//    }


    public void initData() {
        HttpSession session = this.req.getSession();
        User user = (User) session.getAttribute("user");
        // 获得用户全部属性
        user = getUser(user.getEmail());
        // 获得网盘数据
        PanData panData = getPanData(user.getUid());
        // 获得文件数据
        List<UserFile> fileList = getUserFile(user.getUid(), "/");
        // 返回数据
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("username=").append(user.getUsername()).append("&")
                .append("uid").append(user.getUid()).append("&")
                .append("grade=").append(panData.getGrade()).append("&");
        for (UserFile userFile : fileList) {
            stringBuilder
                    .append("file")
                    .append(userFile.getFileName()).append(",")
                    .append(userFile.getFileSize()).append(",")
                    .append(userFile.getCreateTime()).append("|");
        }
        this.writer.println(stringBuilder);
    }

}

