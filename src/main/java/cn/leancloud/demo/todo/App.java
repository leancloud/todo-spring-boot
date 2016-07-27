package cn.leancloud.demo.todo;

import cn.leancloud.EngineSessionCookie;
import cn.leancloud.LeanEngine;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.internal.impl.EngineRequestSign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Hello world!
 */
@Controller
@Configuration
@ServletComponentScan(basePackages="cn.leancloud")
public class WebControllers {

    @RequestMapping(path = "/users/login", method = RequestMethod.GET)
    String loginPage() {
        return "redirect:/login.html";
    }
    @RequestMapping(path = "/users/login", method = RequestMethod.POST)
    String login(String username, String password) {
        try{
            AVUser.logIn(username,password);
        }catch(AVException e){
            e.printStackTrace();
        }
        return "redirect:/login.html";
    }

    public static void main(String[] args) {
        // 初始化AVOSCloud，请保证在整个项目中间只初始化一次
        AVOSCloud.initialize("hiKwg3dGpluLlPtE7Ppc9VaX-gzGzoHsz", "Mgb2yw0HqRX3SwUl9tGH7SUC", "eDKqpGV77A3w0YXPHb5Edt6z");
        // 在请求签名中使用masterKey以激活云代码的最高权限
        EngineRequestSign.instance().setUserMasterKey(true);
        // 打开日志
        AVOSCloud.setDebugLogEnabled(true);
        LeanEngine.addSessionCookie(new EngineSessionCookie("bedstone", 160000, false));

        if ("development".equals(System.getenv("LEANCLOUD_APP_ENV"))) {
            // 如果是开发环境，则设置 AVCloud.callFunction 和 AVCloud.rpcFunction 调用本地云函数实现
            // 如果需要本地开发时调用云端云函数实现，则注释掉下面语句。
            LeanEngine.setLocalEngineCallEnabled(true);
        }
        SpringApplication.run(WebControllers.class, args);
    }

}
