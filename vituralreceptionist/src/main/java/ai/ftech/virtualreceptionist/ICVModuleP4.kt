package ai.ftech.virtualreceptionist

interface ICVModuleP4 {
    fun onConnecting(message: String){}
    fun onDisconnect(message: String){}
    fun onDetectFace(isOneUser: DetectFace, count: Int){}
}


enum class DetectFace {
    NONE, ONE_USER, MULTI_USER
}