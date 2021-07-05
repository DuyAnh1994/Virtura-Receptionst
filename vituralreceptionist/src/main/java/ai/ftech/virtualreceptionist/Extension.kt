package ai.ftech.virtualreceptionist

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> Call<T>.getResultApi(onSuccess: (T) -> Unit, onFail: (message: String) -> Unit = {}) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            response.body()?.let {
                onSuccess.invoke(it)
            } ?: run {
                onFail.invoke("Response null")
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onFail.invoke(t.message!!)
        }
    })
}