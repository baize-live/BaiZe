package live.baize.server.service.utils;

import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class PasswdUtil {

    private String getSHA256Str(String str) {
        MessageDigest messageDigest;
        String SHA256Str = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
            SHA256Str = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return SHA256Str;
    }

    public String generatePassword(String password, String passwdSalt) {
        return getSHA256Str(password + passwdSalt);
    }
}
