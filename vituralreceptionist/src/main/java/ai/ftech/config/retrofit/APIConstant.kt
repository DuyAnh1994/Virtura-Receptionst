package ai.ftech.config.retrofit

import ai.ftech.data.source.remote.service.ICVModuleService
import ai.ftech.data.source.remote.service.ICometService
import ai.ftech.data.source.remote.service.IFptService

object APIConstant {

    const val BASE_URL_STREAM_RTSP = "rtsp://..."
    const val BASE_URL_CV_MODULE_P4 = "cv module"
    const val BASE_URL_FPT_SERVICE = "https://api.fpt.ai/hmi/asr/general"
    const val BASE_URL_COMET = "comet"

    fun getCVModuleService() = RetrofitClient.getClient(BASE_URL_CV_MODULE_P4)?.create(ICVModuleService::class.java)

    fun getFptService() = RetrofitClient.getClient(BASE_URL_FPT_SERVICE)?.create(IFptService::class.java)

    fun getCometService() = RetrofitClient.getClient(BASE_URL_COMET)?.create(ICometService::class.java)
}