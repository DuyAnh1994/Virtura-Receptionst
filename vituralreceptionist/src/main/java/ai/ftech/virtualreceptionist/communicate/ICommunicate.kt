package ai.ftech.virtualreceptionist.communicate

interface ICommunicate {
    fun onConnecting(message: String) {}
    fun onDisconnect(message: String) {}
}