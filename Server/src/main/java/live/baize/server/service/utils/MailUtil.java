package live.baize.server.service.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Slf4j
@Component
@PropertySource("classpath:config.properties")
public class MailUtil {
    @Value("${mail.sendMail}")
    private String sendMail;
    @Value("${mail.authCode}")
    private String authCode;
    @Value("${mail.smtpHost}")
    private String smtpHost;
    @Value("${mail.smtpPort}")
    private String smtpPort;

    private void sendMail(String receiver, String subject, String content) throws Exception {
        // ========== 创建参数配置, 用于连接邮件服务器的参数配置 //
        Properties property = new Properties();
        // 使用的协议（JavaMail规范要求）
        property.put("mail.transport.protocol", "smtp");
        property.put("mail.smtp.host", smtpHost);
        property.put("mail.smtp.port", smtpPort);
        property.put("mail.smtp.auth", "true");
        property.put("mail.smtp.ssl.enable", "true");
        property.put("mail.smtp.starttls.enable", "true");
        property.put("mail.smtp.starttls.required", "true");
        property.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(property);
        String charset = "UTF-8";
        String username = "白泽官方(BaiZe)";
        // ========== 配置邮件信息 ============ //
        MimeMessage message = new MimeMessage(session);
        // From:发件人
        message.setFrom(new InternetAddress(sendMail, username, charset));
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
        transport.connect(sendMail, authCode);
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

    public void sendKeepAliveMail(Integer day) {
        String subject = "BaiZe 服务器保活邮件";
        String content = "服务器已经稳定运行了" + day + "天.\n每天都要保持开心呀... ";
        try {
            sendMail(sendMail, subject, content);
        } catch (Exception e) {
            log.error("邮件系统出现异常.\n" + e.getMessage());
        }
    }

}

