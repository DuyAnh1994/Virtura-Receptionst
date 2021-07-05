package ai.ftech.virtualreceptionist.communicate

interface IFptVR : ICommunicate {
    fun onReceiverFromFPT(message: String) {}
    fun speechToText() {}
}