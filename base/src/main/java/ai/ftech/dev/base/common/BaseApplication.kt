package ai.ftech.dev.base.common

import android.app.Application

abstract class BaseApplication : Application() {

    companion object {
        var appInstance: Application? = null
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

}