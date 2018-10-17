package com.eui.library.event;

/**
 * Created by edisonz on 17-11-15.
 */

public interface IBus {
    void register(Object object);
    void unregister(Object object);
    void post(IEvent event);
    void postSticky(IEvent event);

    interface IEvent {
        int getTag();
    }
}
