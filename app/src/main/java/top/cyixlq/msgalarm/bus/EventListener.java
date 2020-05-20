package top.cyixlq.msgalarm.bus;

public interface EventListener<T> {

    void onEvent(T data);

}
