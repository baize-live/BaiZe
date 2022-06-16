package top.byze.utils;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * @author CodeXS
 */
public class MailUtil {
    /**
     * 参数信息
     * SENDER 发件人邮箱
     * AUTH_CODE 授权码
     * SMTP_HOST 邮箱服务器
     * SMTP_PORT 服务器端口
     */
    private static final String SENDER = "1921676794@qq.com";
    private static final String AUTH_CODE = "qqumdhxxrhzsdfcb";
    private static final String SMTP_HOST = "smtp.qq.com";
    private static final String SMTP_PORT = "465";
    private static final String SMTP_AUTH = "true";
    private static final String SMTP_SSL = "true";

    private static final String CHARSET = "UTF-8";
    private static final String USERNAME = "白泽官方(BaiZe)";
    private static final String SUBJECT = "BaiZe 验证电子邮箱";
    private final String receiver;
    private String content = "感谢您注册白泽账号 \n \n 您的验证码为：\n";

    public MailUtil(String receiver, String verificationCode) {
        this.receiver = receiver;
        this.content += verificationCode;
    }

    public void sendMain() throws Exception {
        // ========== 创建参数配置, 用于连接邮件服务器的参数配置
        // 参数配置
        Properties property = new Properties();
        // 使用的协议（JavaMail规范要求）
        property.setProperty("mail.transport.protocol", "smtp");
        // 发件人的邮箱的 SMTP 服务器地址
        property.setProperty("mail.smtp.host", SMTP_HOST);
        // 发件人的邮箱的 SMTP 服务器端口
        property.setProperty("mail.smtp.port", SMTP_PORT);
        // 需要请求认证
        property.setProperty("mail.smtp.auth", SMTP_AUTH);
        // 使用SSL安全连接
        property.setProperty("mail.smtp.ssl.enable", SMTP_SSL);
        // 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(property);

        // ========== 配置邮件信息 ============ //
        // 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // From:发件人
        message.setFrom(new InternetAddress(SENDER, USERNAME, CHARSET));
        // To:收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiver, receiver, CHARSET));
        // Subject: 邮件主题
        message.setSubject(SUBJECT, CHARSET);
        // Content: 邮件正文
        message.setContent(content, "text/html;charset=UTF-8");
        // 设置发件时间
        message.setSentDate(new Date());
        // 保存设置
        message.saveChanges();

        // 根据 Session获取邮件传输对象
        Transport transport = session.getTransport();
        // 连接服务器
        transport.connect(SENDER, AUTH_CODE);
        // 发送邮件,发到所有的收件地址,message.getAllRecipients()获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());
        // 关闭连接
        transport.close();
    }
}

