package test;

import com.alibaba.fastjson.JSON;
import site.edolphin.compiler.JavaStringCompiler;
import site.edolphin.invoker.ApiInvokerGenerator;

import java.lang.reflect.Method;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        ApiInvokerGenerator invGen = new ApiInvokerGenerator(User.class);
        Class<?> clazz = invGen.generate();
        User user = (User) clazz.newInstance();
        user.setId("set id");
        user.setCreated(123L);
        System.out.println(user);

        String json = JSON.toJSON(user).toString();
        System.out.println(json);
        user = (User)JSON.parseObject(json, User.class);
        System.out.println(user);
    }

    public static void testCompile() throws Exception {
        JavaStringCompiler compiler = new JavaStringCompiler();
        Map<String, byte[]> results = compiler.compile("UserProxy.java", JAVA_SOURCE_CODE);
        Class<?> clazz = compiler.loadClass("site.edolphin.UserProxy", results);
        // try instance:
        User user = (User) clazz.newInstance();
        user.setId("test");

        for (Method method : User.class.getDeclaredMethods()) {
            System.out.println(method.getName());
        }

    }

    static final String JAVA_SOURCE_CODE = "/* a single java source file */   "
            + "package site.edolphin;                                            "
            + "public class UserProxy implements test.User {                     "
            + "    boolean _dirty = false;                                    "
            + "    public void setId(String id) {                             "
            + "        setDirty(true);                                        "
            + "        System.out.println(\"setId\");                         "
            + "    }                                                          "
            + "    public void setName(String name) {                         "
            + "        setDirty(true);                                        "
            + "    }                                                          "
            + "    public void setCreated(long created) {                     "
            + "        setDirty(true);                                        "
            + "    }                                                          "
            + "    public void setDirty(boolean dirty) {                      "
            + "        this._dirty = dirty;                                   "
            + "    }                                                          "
            + "    public boolean isDirty() {                                 "
            + "        return this._dirty;                                    "
            + "    }                                                          "
            + "}";
}

//class Test{
//    String test;
//}
