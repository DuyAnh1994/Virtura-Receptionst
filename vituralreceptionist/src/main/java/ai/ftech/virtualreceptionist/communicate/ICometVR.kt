package ai.ftech.virtualreceptionist.communicate

interface ICometVR : ICommunicate{
    fun onReceiverFromComet(message: String) {}
}