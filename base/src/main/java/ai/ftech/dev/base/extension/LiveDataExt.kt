package ai.ftech.dev.base.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        }
    )
}

fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>

fun <E> LiveData<List<E>>.isEmptyList(): Boolean {
    return value.isNullOrEmpty()
}

fun <T> MutableLiveData<T>.postSelf() {
    postValue(this.value)
}

fun <T> MutableLiveData<T>.setSelf() {
    value = this.value
}

fun <E> MutableLiveData<E>.postIfChanged(newValue: E) {
    if (this.value != newValue) {
        postValue(this.value)
    }
}

fun <E> MutableLiveData<E>.setIfChanged(newValue: E) {
    if (this.value != newValue) {
        value = this.value
    }
}

fun MutableLiveData<Boolean>.postReverseBoolean() {
    val currentValue = value ?: false
    postValue(!currentValue)
}

fun MutableLiveData<Boolean>.setReverseBoolean() {
    val currentValue = value ?: false
    value = !currentValue
}