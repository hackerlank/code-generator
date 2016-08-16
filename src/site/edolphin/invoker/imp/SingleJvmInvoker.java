package site.edolphin.invoker.imp;

import site.edolphin.container.ObjectContainer;
import site.edolphin.invoker.RpcInvoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by edolphin on 16-8-15.
 */
public class SingleJvmInvoker implements RpcInvoker {

    private static SingleJvmInvoker invoker = new SingleJvmInvoker();

    public static SingleJvmInvoker getInvoker() {
        return invoker;
    }

    @Override
    public Object invoke(String serviceName, String methodName, Object... params) {
        try {
            Object obj = ObjectContainer.getContainer().getObject(serviceName + "Service");
            Class<?> impClazz = obj.getClass();
            Class<?>[] paramsTypes = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++) {
                paramsTypes[i] = params[i].getClass();
            }

            Method impMethod = impClazz.getDeclaredMethod(methodName, paramsTypes);
            return impMethod.invoke(obj, params);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.out.println(e.toString());
            return null;
        }
    }
}
