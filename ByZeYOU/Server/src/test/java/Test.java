import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
    /**
     * 日志输出
     */
    private static final Logger log = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        log.warn("warn");
        log.info("info");
        log.error("error");
    }

}
