package ai.ftech.virtualreceptionist

import ai.ftech.virtualreceptionist.communicate.ICVModuleP4VR
import ai.ftech.virtualreceptionist.communicate.ICometVR
import ai.ftech.virtualreceptionist.communicate.IFptVR

interface IVirtualReceptionist {

    fun test()

    fun addCvModuleP4Listener(listener: ICVModuleP4VR)

    fun removeCvModuleP4Listener()

    fun addFPTServiceListener(listener: IFptVR)

    fun removeFPTServiceListener()

    fun addCometListener(listener: ICometVR)

    fun removeCometListener()

    fun openCamera()
}
