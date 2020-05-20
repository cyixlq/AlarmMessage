package top.cyixlq.msgalarm.bus

import androidx.annotation.Nullable

import java.util.ArrayList

class EventWrapper<T> {

    private val listeners = ArrayList<EventListener<T>>()

    fun observe(listener: EventListener<T>) {
        this.listeners.add(listener);
    }

    fun post(data: T) {
        for (listener in listeners) {
            listener.onEvent(data)
        }
    }

    fun unObserve(listener: EventListener<T>) {
        listeners.remove(listener)
    }

}
