package ai.ftech.data.source.remote.service

import ai.ftech.data.source.IApiService
import ai.ftech.data.source.remote.fpt.SpeechToTextRequest
import ai.ftech.data.source.remote.fpt.SpeechToTextResponse
import retrofit2.Call
import retrofit2.http.Body

interface IFptService : IApiService {

    fun speechToText(@Body request: SpeechToTextRequest): Call<SpeechToTextResponse>
}