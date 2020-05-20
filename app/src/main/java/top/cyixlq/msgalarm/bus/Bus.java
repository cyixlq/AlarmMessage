package top.cyixlq.msgalarm.bus;

import java.util.HashMap;
import java.util.Map;

public enum Bus {

    INSTANCE;

    private Map<String, EventWrapper<Object>> eventMap = new HashMap<>();

    public <T> EventWrapper<T> with(String tag, Class<T> clazz) {
        if (!eventMap.containsKey(tag)) {
            eventMap.put(tag, new EventWrapper<>());
        }
        return (EventWrapper<T>) eventMap.get(tag);
    }

    public EventWrapper<Object> with(String tag) {
        return with(tag, Object.class);
    }

}
