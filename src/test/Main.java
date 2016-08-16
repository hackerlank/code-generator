package test;

import com.alibaba.fastjson.JSON;
import site.edolphin.Config;
import site.edolphin.ConstValues;
import site.edolphin.annotation.ServiceImp;
import site.edolphin.annotation.ServiceInterface;
import site.edolphin.compiler.JavaStringCompiler;
import site.edolphin.container.ObjectContainer;
import site.edolphin.invoker.ApiInvokerGenerator;
import site.edolphin.invoker.RpcInvoker;
import site.edolphin.invoker.imp.SingleJvmInvoker;
import site.edolphin.util.reflection.ClassUtils;
import site.edolphin.util.reflection.PackageScanner;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static void initServiceInterface(List<Class> scanedClasses) throws Exception {
        List<Class> serviceInterfaces = scanedClasses.stream()
                .filter(clazz -> clazz.getAnnotation(ServiceInterface.class) != null)
                .collect(Collectors.toList());

        ApiInvokerGenerator invokerGenerator = new ApiInvokerGenerator();
        for (Class serviceInterface : serviceInterfaces) {
            Class<?> consumerClazz = invokerGenerator.generate(serviceInterface);
            // TODO cache the generated class
            Object comsumer = consumerClazz.newInstance();

            String objectName = ClassUtils.getServiceName(serviceInterface);
            ObjectContainer.getContainer().putObject(objectName, comsumer);
        }
    }

    private static void initServiceImp(List<Class> scanedClassed) throws Exception {
        List<Class> serviceImps = scanedClassed.stream()
                .filter(aClass -> aClass.getAnnotation(ServiceImp.class) != null)
                .collect(Collectors.toList());

        for (Class<?> serviceImp : serviceImps) {
            ServiceImp sia = serviceImp.getAnnotation(ServiceImp.class);
            if (sia == null)
                throw new NullPointerException("service " + serviceImp.getName() + "'s ServiceImp not specify the interface class");

            String objectName = ClassUtils.getServiceName(sia.intface());
            if (objectName != null)
                objectName += "Service";

            Object service = serviceImp.newInstance();

            ObjectContainer.getContainer().putObject(objectName, service);
        }
    }

    public static ObjectContainer init(Config config) throws Exception {
        ObjectContainer container = ObjectContainer.getContainer();

        RpcInvoker invoker = new SingleJvmInvoker();
        container.putObject(ConstValues.INVOKER, invoker);

        for (String s : config.getServiceInterfacePackages()) {
            List<Class> scanedClasses = PackageScanner.getClasses(s);

            initServiceInterface(scanedClasses);

            // this shoulde be init on the server side
            initServiceImp(scanedClasses);
        }
        return container;
    }

    public static void main(String[] args) throws Exception {
        Config config = new Config();
        config.addServiceInterfacePackage("test");
        ObjectContainer container = init(config);
        User user = (User)container.getObject("User");
        user.setId("asdf");
        System.out.println(user.hello("world"));
    }


    //region ----------------------------------------test-----------------------
    public static void testGenerator() throws Exception {
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

    //endregion --------------------------------------------------------
}

