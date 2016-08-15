package site.edolphin.invoker.imp;

import site.edolphin.invoker.RpcInvoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by edolphin on 16-8-15.
 */
public class SingleJvmInvoker implements RpcInvoker {

    @Override
    public Object invoke(Class<?> clazz, Method method, Object... params) {
        try {
            Class<?> impClazz = Class.forName(clazz.getName() + "Service");
            Method impMethod = impClazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
            Object obj = impClazz.newInstance();
            return impMethod.invoke(obj, params);
        } catch (ClassNotFoundException e) {
            return null;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            return null;
        }
    }
}
