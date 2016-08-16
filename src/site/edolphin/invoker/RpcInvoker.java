package site.edolphin.invoker;

/**
 * Created by edolphin on 16-8-15.
 */
public interface RpcInvoker {
    Object invoke(String serviceName, String methodName, Object... params);
}
