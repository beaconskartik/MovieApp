package com.movie.app.utils;

import android.util.Log;

public class SubscriberImpl<T> extends rx.Subscriber<T> {

    protected final String prefix;
    protected final String tag;

    public SubscriberImpl(String tag, String prefix) {
        this.tag = tag;
        this.prefix = prefix;
    }

    public void onCompleted() {
        Log.i(tag, "onCompleted: ");
    }

    public void onError(Throwable e) {
        Log.w(tag, prefix + " onErrorImpl: " + e.getMessage(), e);
    }

    public void onNext(T obj) {
        Log.i(tag, "onNext" + prefix + objectToString(obj));
    }

    private String objectToString(T obj) {
        if (obj != null) {
            return obj.toString();
        } else {
            return "";
        }
    }
}