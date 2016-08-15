package site.edolphin.invoker;

import site.edolphin.compiler.JavaStringCompiler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by edolphin on 16-8-15.
 */
public class ApiInvokerGenerator {

    private Class<?> clazz;

    public ApiInvokerGenerator(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> generate() {
        return this.generate(clazz);
    }

    public Class<?> generate(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();

        String packageName = clazz.getPackage().getName();
        sb.append("package " + packageName + ".invoker;");

        String className = clazz.getSimpleName();
        sb.append("public class " + className + "Invoker implements " + clazz.getName() + "{");

        generateMethods(clazz, sb);

        sb.append("}");

        System.out.println(sb.toString());

        JavaStringCompiler compiler = new JavaStringCompiler();
        try {
            Map<String, byte[]> results = compiler.compile(className + "Invoker" + ".java", sb.toString());
            Class<?> res = compiler.loadClass(packageName + ".invoker." + className + "Invoker", results);
            return res;
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private void generateMethods(Class<?> clazz, StringBuilder sb) {
        for (Method method : clazz.getDeclaredMethods()) {
            sb.append("public " + method.getReturnType().getName() + " " + method.getName() + "(");
            generateMethodParams(method, sb);
            sb.append(") {System.out.println(\"in invoker:" + method.getName() + "\");");
            if (!method.getReturnType().equals(void.class))
                sb.append("return ;");      //TODO
            sb.append("}");
        }
    }

    private void generateMethodParams(Method method, StringBuilder sb) {
        Class<?>[] pts = method.getParameterTypes();
        if (pts.length > 0)
            sb.append(pts[0].getName() + " var" + 0);
        for (int i = 1; i < pts.length; i++) {
            sb.append(", " + pts[i].getName() + " var" + i);
        }
    }
}
