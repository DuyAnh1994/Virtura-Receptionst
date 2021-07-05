package ai.ftech.data.repository.fpt

import ai.ftech.config.retrofit.APIConstant

class FptRepoImpl : IFptRepo {

    private val service = APIConstant.getFptService()!!

    override fun speechToText() {
//        service.speechToText()
    }
}
