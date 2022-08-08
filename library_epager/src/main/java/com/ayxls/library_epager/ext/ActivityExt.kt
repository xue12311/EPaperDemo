package com.ayxls.library_epager.ext

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ayxls.library_epager.R
import com.lxj.xpopup.XPopup


fun Activity.showPermissionDialog(
    strTitle: String,
    strMessage: String,
    strConfirmText: String? = getString(R.string.esptouch2_confirm),
    onConfirm: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null
) {
    XPopup.Builder(this)
        //是否在消失的时候销毁资源，默认false。
        .isDestroyOnDismiss(true)
        .asConfirm(strTitle, strMessage,
            getString(R.string.text_cancel), strConfirmText, {
                onConfirm?.invoke()
            }, {
                onCancel?.invoke()
            }, false
        ).show()
}


//
///**
// * 单个权限 获取
// * @param mPermission
// * @param onPermissionGranted 权限申请成功
// * @param onPermissionDenied 被拒绝且未勾选不再询问
// * @param onPermissionExplained 被拒绝且勾选不再询问
// */
//fun ComponentActivity.requestPermission(
//    mPermission: String,
//    onPermissionGranted: ((String) -> Unit)? = null,
//    onPermissionDenied: ((String) -> Unit)? = null,
//    onPermissionExplained: ((String) -> Unit)? = null
//) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        //判断是否已经授权
//        if (ContextCompat.checkSelfPermission(
//                this,
//                mPermission
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            onPermissionGranted?.invoke(mPermission)
//        } else {
//            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//                //权限获取成功
//                if (isGranted) {
//                    onPermissionGranted?.invoke(mPermission)
//                } else { //权限获取失败
//                    //是否已经勾选 禁止后不再询问
//                    val isDenied = shouldShowRequestPermissionRationale(mPermission)
//                    //被拒接 且 没勾选不再询问
//                    if (isDenied) {
//                        onPermissionDenied?.invoke(mPermission)
//                        //被拒接 且 勾选了不再询问
//                    } else {
//                        onPermissionExplained?.invoke(mPermission)
//                    }
//                }
//            }.launch(mPermission)
//        }
//    } else {
//        onPermissionGranted?.invoke(mPermission)
//    }
//}

//fun Fragment.showPermissionDialog(
//    strTitle: String,
//    strMessage: String,
//    strConfirmText: String? = getString(R.string.esptouch2_confirm),
//    onConfirm: (() -> Unit)? = null,
//    onCancel: (() -> Unit)? = null
//) {
//    activity?.showPermissionDialog(strTitle, strMessage, strConfirmText, onConfirm, onCancel)
//}


///**
// * 单个权限 获取
// * @param mPermission
// * @param onPermissionGranted 权限申请成功
// * @param onPermissionDenied 被拒绝且未勾选不再询问
// * @param onPermissionExplained 被拒绝且勾选不再询问
// */
//fun Fragment.requestPermission(
//    mPermission: String,
//    onPermissionGranted: ((String) -> Unit)? = null,
//    onPermissionDenied: ((String) -> Unit)? = null,
//    onPermissionExplained: ((String) -> Unit)? = null
//) {
//    activity?.requestPermission(
//        mPermission,
//        onPermissionGranted,
//        onPermissionDenied,
//        onPermissionExplained
//    )
//}