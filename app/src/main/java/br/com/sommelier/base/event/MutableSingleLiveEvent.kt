package br.com.sommelier.base.event

import androidx.annotation.MainThread

class MutableSingleLiveEvent<T> : SingleLiveEvent<T> {

    constructor() : super()
    constructor(value: T) : super(value)

    @MainThread
    public override fun emit(value: T?) {
        super.emit(value)
    }

    @MainThread
    public override fun setValue(value: T?) {
        super.emit(value)
    }

    public override fun post(value: T?) {
        super.post(value)
    }

    public override fun postValue(value: T?) {
        super.post(value)
    }

    fun asSingleLiveEvent(): SingleLiveEvent<T> = this
}
