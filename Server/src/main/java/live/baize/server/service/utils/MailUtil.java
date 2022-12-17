package live.baize.server.service.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * @author CodeXS
 */

@Component
@PropertySource("classpath:config.properties")
public class MailUtil {
    @Value("${mail.sender}")
    private String sender;
    @Value("${mail.authCode}")
    private String authCode;
    @Value("${mail.smtpHost}")
    private String smtpHost;
    @Value("${mail.smtpPort}")
    private String smtpPort;
    @Value("${mail.smtpAuth}")
    private String smtpAuth;
    @Value("${mail.smtpSsl}")
    private String smtpSsl;
    @Value("${mail.charset}")
    private String charset;
    @Value("${mail.username}")
    private String username;

    private void sendMail(String receiver, String subject, String content) throws Exception {
        // ========== 创建参数配置, 用于连接邮件服务器的参数配置
        // 参数配置
        Properties property = new Properties();
        // 使用的协议（JavaMail规范要求）
        property.setProperty("mail.transport.protocol", "smtp");
        // 发件人的邮箱的 SMTP 服务器地址
        property.setProperty("mail.smtp.host", smtpHost);
        // 发件人的邮箱的 SMTP 服务器端口
        property.setProperty("mail.smtp.port", smtpPort);
        // 需要请求认证
        property.setProperty("mail.smtp.auth", smtpAuth);
        // 使用SSL安全连接
        property.setProperty("mail.smtp.ssl.enable", smtpSsl);
        // 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(property);

        // ========== 配置邮件信息 ============ //
        // 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // From:发件人
        message.setFrom(new InternetAddress(sender, username, charset));
        // To:收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiver, receiver, charset));
        // Subject: 邮件主题
        message.setSubject(subject, charset);
        // Content: 邮件正文
        message.setContent(content, "text/html;charset=UTF-8");
        // 设置发件时间
        message.setSentDate(new Date());
        // 保存设置
        message.saveChanges();

        // 根据 Session获取邮件传输对象
        Transport transport = session.getTransport();
        // 连接服务器
        transport.connect(sender, authCode);
        // 发送邮件,发到所有的收件地址,message.getAllRecipients()获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());
        // 关闭连接
        transport.close();
    }

    public void sendVerifyCode(String email, String verifyCode) throws Exception {
        String subject = "BaiZe 验证电子邮箱";
        String content = "感谢您注册白泽账号 \n \n 您的验证码为：\n" + verifyCode;
        sendMail(email, subject, content);
    }

}

