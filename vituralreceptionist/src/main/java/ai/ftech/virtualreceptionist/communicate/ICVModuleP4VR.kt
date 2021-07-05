package ai.ftech.virtualreceptionist.communicate

interface ICVModuleP4VR : ICommunicate{
    fun onDetectFace(isOneUser: DetectFace, count: Int) {}
}


enum class DetectFace {
    NONE, ONE_USER, MULTI_USER
}