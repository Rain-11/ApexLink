import cn.hutool.core.lang.UUID;

/**
 * @ClassName: Test
 * @Description: 测试
 * @author: CrazyRain
 * @date: 2024/4/26 上午8:51
 */

public class Test {

    public static void main(String[] args) {
        String string = UUID.randomUUID().toString().replace("-", "");
        System.out.println(string);
    }
}
