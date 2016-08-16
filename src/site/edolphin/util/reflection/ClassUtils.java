package site.edolphin.util.reflection;

import site.edolphin.annotation.ServiceInterface;

/**
 * Created by edolphin on 16-8-16.
 */
public class ClassUtils {

    public static String getServiceName(Class<?> clazz) {
        ServiceInterface sia = clazz.getAnnotation(ServiceInterface.class);
        if (sia == null)
            return "";
        if (!sia.value().equals(""))
            return sia.value();
        return clazz.getSimpleName();
    }
}
