package ai.ftech.virtualreceptionist

interface IFPT {
    fun onConnecting(message: String){}
    fun onDisconnect(message: String){}
    fun onReceiverFromFPT(message: String){}
}