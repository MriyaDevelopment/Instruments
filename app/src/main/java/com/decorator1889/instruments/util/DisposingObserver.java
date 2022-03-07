package com.decorator1889.instruments.util;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DisposingObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {
        DisposableManager.add(d);
    }

    @Override
    public void onNext(T value) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
