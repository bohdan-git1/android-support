package com.rapidzz.yourmusicmap.other.util;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;


public final class RxBus {
    private static RxBus rxBus;
    private final PublishSubject<Object> bus = PublishSubject.create();

    private RxBus() {

    }

    public static RxBus defaultInstance() {
        if (rxBus == null) {
            rxBus = new RxBus();
        }
        return rxBus;
    }

    public void send(final Object event) {
        bus.onNext(event);
    }

    public Observable<Object> toObservable() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}
