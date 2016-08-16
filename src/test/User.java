package test;

import site.edolphin.annotation.ServiceInterface;

/**
 * Created by edolphin on 16-8-15.
 */
@ServiceInterface
public interface User {
        void setId(String id);
        void setName(String name);
        void setCreated(long created);
        void setDirty(boolean dirty);
        String hello(String name);
}

