package top.ccyy.factory;

import java.util.HashMap;
import java.util.Map;


/**
 * 单例工厂
 */
public class SingletonFactory {

    private static Map<Class<?>, Object> objectMap = new HashMap<>();

    public SingletonFactory() {
    }

    public static synchronized <T> T getInstance(Class<T> clazz) {
        Object singleton = objectMap.get(clazz);
        if (singleton == null) {
            try {
                singleton = clazz.newInstance();
                objectMap.put(clazz, singleton);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return clazz.cast(singleton);
    }
}
