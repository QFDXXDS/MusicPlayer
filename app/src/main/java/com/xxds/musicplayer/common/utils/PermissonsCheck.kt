package com.xxds.musicplayer.common.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


fun lacksPermissions(context: Context, permissions: Array<String>): Boolean {

    return permissions.any { lacksPermission(context,it)  }

}

private fun lacksPermission(context: Context, permission: String): Boolean {

    return ContextCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_DENIED
}