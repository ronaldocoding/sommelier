package br.com.sommelier.base.event

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

open class SingleLiveEvent<T> : LiveData<T> {

    constructor() : super()

    constructor(value: T) : super(value)

    private var pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }

        super.observe(owner) {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        }
    }

    /**
     * Used to dispatch the value to main-thread.
     * This value arrive first than value sent in [post] function
     * because is dispatched directly to main-thread.
     *
     * @see [MutableLiveData.setValue] documentation
     */
    @MainThread
    protected open fun emit(value: T?) {
        pending.set(true)
        super.setValue(value)
    }

    /**
     * Used to dispatch the value in any thread to main-thread.
     *
     * @see [MutableLiveData.postValue] documentation
     */
    protected open fun post(value: T?) {
        pending.set(true)
        super.postValue(value)
    }

    companion object {
        private const val TAG = "SingleLiveEvent"
    }
}
