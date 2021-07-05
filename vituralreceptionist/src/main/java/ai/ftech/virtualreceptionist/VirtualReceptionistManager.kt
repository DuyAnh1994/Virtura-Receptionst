package ai.ftech.virtualreceptionist

import ai.ftech.virtualreceptionist.communicate.DetectFace
import ai.ftech.virtualreceptionist.communicate.ICVModuleP4VR
import ai.ftech.virtualreceptionist.communicate.ICometVR
import ai.ftech.virtualreceptionist.communicate.IFptVR
import kotlinx.coroutines.*

class VirtualReceptionistManager : IVirtualReceptionist {

    companion object {
        private val TAG = VirtualReceptionistManager::class.java.simpleName

        private var instance: VirtualReceptionistManager? = null

        fun getInstance(): VirtualReceptionistManager {
            return instance ?: VirtualReceptionistManager()
        }
    }

    private var cvModuleP4VR: ICVModuleP4VR? = null
    private var fpt: IFptVR? = null
    private var cometVR: ICometVR? = null

    override fun test() {
        CoroutineScope(Dispatchers.IO).launch {
            for (i in 0..10000) {
                delay(500)
                withContext(Dispatchers.Main) {
                    cvModuleP4VR?.onDetectFace(DetectFace.ONE_USER, i)
                }
            }
        }
    }

    override fun addCvModuleP4Listener(listener: ICVModuleP4VR) {
        this.cvModuleP4VR = listener
        cvModuleP4VR?.onConnecting("connecting...")
    }

    override fun removeCvModuleP4Listener() {
        cvModuleP4VR?.onConnecting("disconnect...")
        this.cvModuleP4VR = null
    }

    override fun addFPTServiceListener(listener: IFptVR) {
        this.fpt = listener
        fpt?.onConnecting("connecting...")
    }

    override fun removeFPTServiceListener() {
        cvModuleP4VR?.onConnecting("disconnect...")
        this.cvModuleP4VR = null
    }

    override fun addCometListener(listener: ICometVR) {
        this.cometVR = listener
        cometVR?.onConnecting("connecting...")
    }

    override fun removeCometListener() {
        cometVR?.onConnecting("disconnect...")
        this.cometVR = null
    }

    override fun openCamera() {

    }
}
