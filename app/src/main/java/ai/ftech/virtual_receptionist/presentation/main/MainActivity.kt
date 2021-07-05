package ai.ftech.virtual_receptionist.presentation.main

import ai.ftech.dev.base.common.BaseActivity
import ai.ftech.dev.base.common.GetLayoutId
import ai.ftech.virtual_receptionist.R
import ai.ftech.virtual_receptionist.databinding.ActivityMainBinding
import ai.ftech.virtualreceptionist.IVirtualReceptionist
import ai.ftech.virtualreceptionist.VirtualReceptionistManager
import ai.ftech.virtualreceptionist.communicate.DetectFace
import ai.ftech.virtualreceptionist.communicate.ICVModuleP4VR
import ai.ftech.virtualreceptionist.communicate.ICometVR
import ai.ftech.virtualreceptionist.communicate.IFptVR
import android.Manifest
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.VideoResult


@GetLayoutId(R.layout.activity_main)
class MainActivity : BaseActivity<ActivityMainBinding>() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val URL_LIVE_STREAM = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov"
    }
    private val manager : IVirtualReceptionist = VirtualReceptionistManager.getInstance()
    private var isCvModuleP4Connection = MutableLiveData(false)
    private var isFptServiceConnection = MutableLiveData(false)
    private val listPermissionRecordVideo = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private var isCometConnection = MutableLiveData(false)

    override fun onInitBinding() {
    }

    override fun onInitView() {
        manager.test()
//        binding.cameraView.setLifecycleOwner(this)
        registerListenerCameraView()
    }

    override fun observerViewModel() {

    }

    override fun onViewClick(view: View) {
        when (view.id) {
            R.id.btnCVModuleP4 -> {
                isCvModuleP4Connection.value = if (isCvModuleP4Connection.value!!) {
                    manager.removeCvModuleP4Listener()
                    false
                } else {
                    manager.addCvModuleP4Listener(cvModuleP4Listener)
                    true
                }
            }
            R.id.btnFptService -> {
                isFptServiceConnection.value = if (isFptServiceConnection.value!!) {
                    manager.removeFPTServiceListener()
                    false
                } else {
                    manager.addFPTServiceListener(fptServiceListener)
                    true
                }
            }
            R.id.btnComet -> {
                isCometConnection.value = if (isCometConnection.value!!) {
                    manager.removeCometListener()
                    false
                } else {
                    manager.addCometListener(cometListener)
                    true
                }
            }
            R.id.btnOpenCamera -> {
                requestPermission(listPermissionRecordVideo) {
                }
            }
        }
    }

    private fun requestPermission(permissions: Array<String>, onAllow: () -> Unit) {
        doRequestPermission(permissions, onAllow, {
            requestPermission(permissions, onAllow)
        })
    }


    private fun registerListenerCameraView() {
//        binding.cameraView.addCameraListener(object : CameraListener() {
//            override fun onVideoTaken(result: VideoResult) {
//
//            }
//        })
    }

    private val cvModuleP4Listener = object : ICVModuleP4VR {
        override fun onConnecting(message: String) {
            Log.d(TAG, "onConnecting() called with: message = $message")
        }

        override fun onDisconnect(message: String) {
            Log.d(TAG, "onDisconnect() called with: message = $message")
        }

        override fun onDetectFace(isOneUser: DetectFace, count: Int) {
            Log.d(TAG, "onDetectFace() called with: isOneUser = ${isOneUser.name}, count = $count")
        }
    }
    private val fptServiceListener = object : IFptVR {
        override fun onConnecting(message: String) {
            Log.d(TAG, "onConnecting() called with: message = $message")
        }

        override fun onDisconnect(message: String) {
            Log.d(TAG, "onDisconnect() called with: message = $message")
        }

        override fun onReceiverFromFPT(message: String) {
            Log.d(TAG, "onReceiverFromFPT() called with: message = $message")
        }
    }
    private val cometListener = object : ICometVR {
        override fun onConnecting(message: String) {
            Log.d(TAG, "onConnecting() called with: message = $message")
        }

        override fun onDisconnect(message: String) {
            Log.d(TAG, "onDisconnect() called with: message = $message")
        }

        override fun onReceiverFromComet(message: String) {
            Log.d(TAG, "onReceiverFromComet() called with: message = $message")
        }
    }
}
