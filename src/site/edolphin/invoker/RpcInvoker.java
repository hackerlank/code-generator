package site.edolphin.invoker;

import java.lang.reflect.Method;

/**
 * Created by edolphin on 16-8-15.
 */
public interface RpcInvoker {
    Object invoke(Class<?> clazz, Method method, Object... params);
}
