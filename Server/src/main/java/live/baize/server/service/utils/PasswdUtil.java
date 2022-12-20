package live.baize.server.service.utils;

import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class PasswdUtil {
    final String SHA_128 = "SHA-1";
    final String SHA_256 = "SHA-256";

    private String getEncryptStr(String algorithm, String str) {
        MessageDigest messageDigest;
        String encryptStr = null;
        try {
            messageDigest = MessageDigest.getInstance(algorithm);
            byte[] hash = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
            encryptStr = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptStr;
    }

    public String generateFileName(Integer UId, String fileDir, String fileName) {
        return getEncryptStr(SHA_128, UId + fileDir + fileName);
    }

    public String generatePassword(String password, String passwdSalt) {
        return getEncryptStr(SHA_256, password + passwdSalt);
    }
}
