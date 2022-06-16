package top.byze.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class mapTest {

    @Test
    public void test() {
        // 仅仅表示初始容量
        Map<String, String> map = new HashMap<>(2);
        map.put("a", "a");
        map.put("b", "a");
        map.put("c", "a");

    }
}
