package Bean;

import java.net.InetAddress;

public class Vars {
    public static final String LocalData = "src/localData/";
    public static final String imgData = "img/";
    public static InetAddress address;

    static {
        try {
            address = InetAddress.getLocalHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
