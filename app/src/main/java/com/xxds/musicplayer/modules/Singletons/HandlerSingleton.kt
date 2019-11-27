package com.xxds.musicplayer.modules.Singletons

import java.util.logging.Handler

class HandlerSingleton: android.os.Handler() {

    companion object {

        val instance: HandlerSingleton by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { HandlerSingleton() }

    }
}