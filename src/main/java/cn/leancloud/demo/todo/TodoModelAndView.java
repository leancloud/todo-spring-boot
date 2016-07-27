package cn.leancloud.demo.todo;

import com.avos.avoscloud.AVUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.Map;

/**
 * Created by lbt05 on 7/22/16.
 */
public class TodoModelAndView extends ModelAndView {
    public TodoModelAndView() {
        super();
        bindUser();
    }

    public TodoModelAndView(String viewName) {
        super(viewName);
        bindUser();
    }

    public TodoModelAndView(View view) {
        super(view);
        bindUser();
    }

    public TodoModelAndView(String viewName, Map<String, ?> model) {
        super(viewName, model);
        bindUser();
    }

    public TodoModelAndView(View view, Map<String, ?> model) {
        super(view, model);
        bindUser();
    }

    public TodoModelAndView(String viewName, Map<String, ?> model, HttpStatus status) {
        super(viewName, model, status);
    }

    public TodoModelAndView(String viewName, String modelName, Object modelObject) {
        super(viewName, modelName, modelObject);
        bindUser();
    }

    public TodoModelAndView(View view, String modelName, Object modelObject) {
        super(view, modelName, modelObject);
        bindUser();
    }

    private void bindUser(){
        if(AVUser.getCurrentUser()!=null){
            this.addObject("user",AVUser.getCurrentUser().getUsername());
        }
    }

}
