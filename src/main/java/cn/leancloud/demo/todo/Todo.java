package cn.leancloud.demo.todo;

import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

/**
 * Created by lbt05 on 7/26/16.
 */

@AVClassName("Todo")
public class Todo extends AVObject {

    public Todo() {
        super();
    }

    public Todo(boolean acl) {
        if (acl) {
            AVACL avacl = new AVACL();
            avacl.setPublicReadAccess(true);
            avacl.setPublicWriteAccess(true);
            this.setACL(avacl);
        }
    }

    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        this.put("content", content);
    }

    public String getAuthor() {
        return getAVUser("author") == null ? null : getAVUser("author").getUsername();
    }

    public void setAuthor(AVUser user) {
        AVACL acl = this.getACL();
        if (user != null) {
            acl.setPublicWriteAccess(false);
            acl.setWriteAccess(user, true);
        }
        this.put("author", user);
    }

    public int getStatus() {
        return getInt("status");
    }

    public void setStatus(int status) {
        this.put("status", status);
    }
}
