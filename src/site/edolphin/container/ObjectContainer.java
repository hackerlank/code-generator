package site.edolphin.container;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by edolphin on 16-8-16.
 */
public class ObjectContainer {

    private static ObjectContainer container = new ObjectContainer();

    public static ObjectContainer getContainer() {
        return container;
    }

    Map<String, Object> objectMap = new HashMap<>();

    public Map<String, Object> getObjectMap() {
        return objectMap;
    }

    public void setObjectMap(Map<String, Object> objectMap) {
        this.objectMap = objectMap;
    }

    public ObjectContainer() {
    }

    public ObjectContainer(Map<String, Object> objectMap) {
        this.objectMap = objectMap;
    }

    public Object getObject(String name) {
        return objectMap.get(name);
    }

    public void putObject(String name, Object obj) {
        objectMap.put(name, obj);
    }
}
