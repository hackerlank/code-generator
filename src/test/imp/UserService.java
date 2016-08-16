package test.imp;

import site.edolphin.annotation.ServiceImp;
import test.User;

/**
 * Created by edolphin on 16-8-16.
 */
@ServiceImp(intface = User.class)
public class UserService implements User {
    @Override
    public void setId(String id) {
        System.out.println("in imp method");
    }

    @Override
    public void setName(String name) {
        System.out.println("in imp method");
    }

    @Override
    public void setCreated(long created) {
        System.out.println("in imp method");
    }

    @Override
    public void setDirty(boolean dirty) {
        System.out.println("in imp method");
    }

    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}
