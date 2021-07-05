package ai.ftech.virtualreceptionist

interface IComet {
    fun onConnecting(message: String) {}
    fun onDisconnect(message: String) {}
    fun onReceiverFromComet(message: String) {}
}