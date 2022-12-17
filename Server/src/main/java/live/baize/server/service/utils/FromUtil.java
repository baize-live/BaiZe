package live.baize.server.service.utils;

import live.baize.server.bean.business.disk.FromMap;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author CodeXS
 */
public class FromUtil {
    public static FromMap parseParam(HttpServletRequest req) {
        FromMap result = new FromMap();
        //创建一个FileItem工厂 通过DiskFileItemFactory对象创建文件上传核心组件
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        upload.setHeaderEncoding("UTF-8");
        try {
            //通过文件上传核心组件解析request请求，获取到所有的FileItem对象
            List<FileItem> fileItemList = upload.parseRequest(req);
            //遍历表单的所有表单项（FileItem） 并对其进行相关操作
            StringBuilder str = new StringBuilder();
            for (FileItem fileItem : fileItemList) {
                //判断这个表单项如果是一个普通的表单项
                if (fileItem.isFormField()) {
                    str.append(fileItem.getString("UTF-8"));
                } else {
                    result.getFileMap().put(fileItem.getName(), fileItem);
                }
            }
            result.getParamMap().put("currentDir", str.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
