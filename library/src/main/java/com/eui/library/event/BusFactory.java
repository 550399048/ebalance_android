package com.eui.library.event;

/**
 * Created by edisonz on 17-11-15.
 */

public class BusFactory {
    private static IBus bus;
    public static IBus getBus() {
        if (bus == null) {
            synchronized (bus) {
                if (bus == null) {
                    bus = new EventBusImpl();
                }
            }
        }
        return bus;
    }
}
