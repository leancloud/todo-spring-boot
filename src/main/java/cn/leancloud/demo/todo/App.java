package cn.leancloud.demo.todo;

import cn.leancloud.EngineSessionCookie;
import cn.leancloud.LeanEngine;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Hello world!
 */

@ServletComponentScan(basePackages = {"cn.leancloud"})
@EnableAutoConfiguration
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        AVObject.registerSubclass(Todo.class);
        // 初始化AVOSCloud，请保证在整个项目中间只初始化一次
        AVOSCloud.initialize("hiKwg3dGpluLlPtE7Ppc9VaX-gzGzoHsz", "Mgb2yw0HqRX3SwUl9tGH7SUC", "eDKqpGV77A3w0YXPHb5Edt6z");
        // 打开日志
        AVOSCloud.setDebugLogEnabled(true);
        LeanEngine.addSessionCookie(new EngineSessionCookie("bedstone","avosSession", 160000, true));

        if ("development".equals(System.getenv("LEANCLOUD_APP_ENV"))) {
            // 如果是开发环境，则设置 AVCloud.callFunction 和 AVCloud.rpcFunction 调用本地云函数实现
            // 如果需要本地开发时调用云端云函数实现，则注释掉下面语句。
            LeanEngine.setLocalEngineCallEnabled(true);
        }
        SpringApplication.run(App.class, args);
    }
}
