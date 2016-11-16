package cn.leancloud.demo.todo;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lbt05 on 7/20/16.
 */
@Controller
public class TodoController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    ModelAndView home() throws AVException {
        return new TodoModelAndView("redirect:/todos");
    }

    @RequestMapping(path = "/todos", method = RequestMethod.GET)
    ModelAndView todos(@RequestParam(required = false, defaultValue = "0") String status) {
        Map<String,Object> attributes = new HashMap<String,Object>();
        AVQuery<Todo> query = Todo.getQuery(Todo.class);
        query.whereEqualTo("status", Integer.parseInt(status));
        query.include("author");
        query.orderByDescending("updatedAt");
        query.limit(1000);
        try{
            List<Todo> result = query.find();
            attributes.put("todos",result);
        }catch (AVException e){
            attributes.put("errorMessage",e.getMessage());
        }
        return new TodoModelAndView("todos", attributes);
    }

    @RequestMapping(path = "todos", method = RequestMethod.POST)
    ModelAndView saveTodo(String content,RedirectAttributes redirectAttrs) {
        AVUser user = AVUser.getCurrentUser();
        Todo todo = new Todo(true);
        todo.setAuthor(user);
        todo.setContent(content);
        try {
            todo.save();
        } catch (AVException e) {
            redirectAttrs.addAttribute("errorMessage",e.getMessage());
        }
        return new TodoModelAndView("redirect:/todos");
    }

    @RequestMapping(path="todos/{id}/mark",method = RequestMethod.POST)
    public ModelAndView markDone(@PathVariable("id")String id,String currentStatus,RedirectAttributes redirectAttrs){
        try {
            Todo todo = Todo.createWithoutData(Todo.class,id);
            todo.put("status",1 - Integer.parseInt(currentStatus));
            todo.save();
        } catch (AVException e) {
            e.printStackTrace();
            redirectAttrs.addAttribute("errorMessage",e.getMessage());
        }
        return new TodoModelAndView("redirect:/todos?status="+currentStatus);
    }

    @RequestMapping(path="todos/{id}",method = RequestMethod.DELETE)
    public ModelAndView deleteNote(@PathVariable("id") String id,String status,RedirectAttributes redirectAttrs){
        try {
            Todo todo = Todo.createWithoutData(Todo.class,id);
            todo.delete();
        } catch (AVException e) {
            e.printStackTrace();
            redirectAttrs.addAttribute("errorMessage",e.getMessage());
        }
        return new TodoModelAndView("redirect:/todos?status="+status);
    }


    @RequestMapping(path = "/users/login", method = RequestMethod.GET)
    ModelAndView loginPage() {
        return new TodoModelAndView("login");
    }

    @RequestMapping(path = "/users/login", method = RequestMethod.POST)
    ModelAndView login(String username, String password) {
        try {
            AVUser.logIn(username, password);
        } catch (AVException e) {
            return new ModelAndView("login","errorMessage",e.getMessage());
        }
        return new ModelAndView("redirect:/todos");
    }

    @RequestMapping(path = "/users/register", method = RequestMethod.POST)
    ModelAndView userRegister(String username, String password) {
        try {
            AVUser user = new AVUser();
            user.setUsername(username);
            user.setPassword(password);
            user.signUp();
            return new TodoModelAndView("todos");
        } catch (AVException e) {
            return new TodoModelAndView("register", "errorMessage", e.getMessage());
        }

    }

    @RequestMapping(path = "users/register",method = RequestMethod.GET)
    ModelAndView registerPage(){
        return new TodoModelAndView("register");
    }

    @RequestMapping(path = "/users/logout", method = RequestMethod.GET)
    ModelAndView userLogout() {
        if (AVUser.getCurrentUser() != null) {
            AVUser.getCurrentUser().logOut();
        }
        return new ModelAndView("redirect:/todos");
    }
}
