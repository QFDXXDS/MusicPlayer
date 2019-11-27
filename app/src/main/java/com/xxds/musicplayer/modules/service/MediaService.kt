package com.xxds.musicplayer.modules.service

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.*
import android.os.*
import android.text.TextUtils
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat.stopForeground
import androidx.core.content.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.xxds.ApplicationMain
import com.xxds.chitjishi.Common.Config.AppConfig.APP_NAME
import com.xxds.chitjishi.Common.net.RetrofitFactory
import com.xxds.chitjishi.Common.net.RetrofitFactory.Companion.LrcInfoRetrofit
import com.xxds.musicplayer.R
import com.xxds.musicplayer.base.Null
import com.xxds.musicplayer.common.utils.lacksPermissions
import com.xxds.musicplayer.modules.entity.MusicEntity
import com.xxds.musicplayer.modules.main.models.RecommendListModel
import com.xxds.musicplayer.modules.main.ui.MainActivity
import com.xxds.musicplayer.modules.main.ui.Welcome
import com.xxds.musicplayer.modules.playing.entities.LrcInfo
import com.xxds.musicplayer.modules.provider.database.provider.RecentStore
import com.xxds.musicplayer.modules.service.MediaService.Companion.LRC_DOWNLOADED
import com.xxds.musicplayer.modules.service.MediaService.Companion.LRC_PATH
import com.xxds.musicplayer.modules.service.MediaService.Companion.REPEAT_ALL
import com.xxds.musicplayer.modules.service.MediaService.Companion.REPEAT_CURRENT
import com.xxds.musicplayer.modules.service.MediaService.Companion.TRACK_ENDED
import com.xxds.musicplayer.modules.service.MusicPlayer.Companion.getAudioId
import com.xxds.musicplayer.modules.service.MusicPlayer.Companion.getTrackName
import com.xxds.musicplayer.modules.service.MusicPlayer.Companion.nextPlay
//import com.xxds.musicplayer.modules.service.MusicPlayer.Companion.play
import com.xxds.musicplayer.modules.service.MusicPlayer.Companion.seek
import com.xxds.musicplayer.modules.service.MusicPlayer.Companion.setRepeatMode
import com.xxds.musicplayer.modules.service.viewmodels.ServiceViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.lang.ref.WeakReference
import java.util.ArrayList
import java.util.HashMap

class MediaService: Service() {

    companion object {

        const val TAG = "MediaService"

        const val TRACK_ENDED = 1
        /**
         * 自动切换下一首
         */
        const val TRACK_WENT_TO_NEXT = 2
        const val RELEASE_WAKELOCK = 3
        const val SERVER_DIED = 4
        const val FADE_DOWN = 6
        const val FADE_UP = 7
        const val LRC_DOWNLOADED = -10


        /**
         * 单曲循环
         */
        const val REPEAT_CURRENT = 1

        /**
         * 顺序播放
         */
        const val REPEAT_ALL = 2

        /**
         * 随机播放
         */
        const val REPEAT_SHUFFLER = 3

        const val MAX_HISTORY_SIZE = 1000

        const val LRC_PATH = "/semusic/lrc/"

        private const val TRACK_NAME = "trackname"
        private const val NOTIFY_MODE_NONE = 0
        private const val NOTIFY_MODE_FOREGROUND = 1
        private const val IDLE_DELAY = 5 * 60 * 1000


    }
    var mRepeatMode = REPEAT_ALL
    var mLastSeekPos: Long = 0
    /**
     * 当前播放列表
     */
    private val mPlaylist = ArrayList<MusicTrack>(100)
    /**
     * 当前播放的音乐的实体
     */
    private var currentMusicEntity: MusicEntity? = null
    /**
     * 当前要播放音乐的地址
     */
    private var mFileToPlay: String = Null
    private var mOpenFailedCounter = 0

    var isPlaying = false
    private lateinit var mPlayer: MultiPlayer

    private lateinit var mNotificationManager: NotificationManager
    private val binder = ServiceStub(this)
    private val intentReceiver = object : BroadcastReceiver() {

        override fun onReceive(p0: Context?, p1: Intent?) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

            handleCommandIntent(p1)
        }
    }

    /**
     * 当前播放音乐的下标
     */
    private var mPlayPos = -1
    private lateinit var mHandlerThread: HandlerThread
    private lateinit var mPlayerHandler: MusicPlayerHandler

    private val mNotificationId = 1000
    private var mServiceInUse = false
    private var mServiceStartId = -1

    private var mNotificationPostTime: Long = 0
    private var mNotifyMode = NOTIFY_MODE_NONE
    private var mPlayListInfo = HashMap<Long, MusicEntity>()

    val viewModel = ServiceViewModel()
    private var mRequestLrc: RequestLrc? = null
    private lateinit var mLrcHandler: Handler
    private lateinit var mUrlHandler: Handler


    private val mLrcThread = Thread(Runnable {
        Looper.prepare()
        mLrcHandler = Handler()
        Looper.loop()
    })
    private val mGetUrlThread = Thread(Runnable {
        Looper.prepare()
        mUrlHandler = Handler()
        Looper.loop()
    })

    private var mPreferences: SharedPreferences? = null
    private var mRecentStore: RecentStore? = null


    override fun onCreate() {
        super.onCreate()

        mGetUrlThread.start()
        mLrcThread.start()

        mHandlerThread = HandlerThread("MusicPlayerHandler", android.os.Process.THREAD_PRIORITY_BACKGROUND)
        mHandlerThread.start()
        mPlayerHandler = MusicPlayerHandler(this,mHandlerThread.looper)
        mPlayer = MultiPlayer(this,mPlayerHandler)

        mPreferences = getSharedPreferences("Service",Context.MODE_PRIVATE)
        mRecentStore = RecentStore.instance

        val filter = IntentFilter().apply {

            addAction(TOGGLE_PAUSE_ACTION)
            addAction(STOP_ACTION)
            addAction(NEXT_ACTION)
            addAction(PREVIOUS_ACTION)
            addAction(PREVIOUS_FORCE_ACTION)
            addAction(REPEAT_ACTION)
            addAction(SHUFFLE_ACTION)
            addAction(TRY_GET_TRACK_INFO)
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(SEND_PROGRESS)
        }
        registerReceiver(intentReceiver,filter)
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        addObserver()
    }

    fun addObserver() {

        viewModel.songInfo.observeForever {

            val song = it.data?.songList!![0]
            val songLink = song.songLink
            val musicEntity = MusicEntity()

            musicEntity.musicName = song.songName
            musicEntity.albumData = song.songPicBig
            musicEntity.artist = song.artistName
            musicEntity.lrc = song.lrcLink
            musicEntity.audioId = song.songId
            musicEntity.size = song.time
            musicEntity.data = song.songLink

            currentMusicEntity = musicEntity
            openFile(songLink!!)


            if (mRequestLrc != null) {
                mLrcHandler.removeCallbacks(mRequestLrc)
            }
            mRequestLrc = RequestLrc(currentMusicEntity?.lrc!!, getMusicEntity())
            mLrcHandler.postDelayed(mRequestLrc, 70)

        }


        viewModel.lrcData.observeForever {


            return@observeForever
            val file = File(Environment.getExternalStorageDirectory().absolutePath + LRC_PATH + currentMusicEntity?.audioId + ".lrc")
            val storageFile = File(Environment.getExternalStorageDirectory().absolutePath + LRC_PATH)

            if (file.exists()) {

                mPlayerHandler.sendEmptyMessage(LRC_DOWNLOADED)
                return@observeForever
            }

            // 部分机型创建目录成功之后才能创建文件
            if (!storageFile.exists()) {
               var aaa = storageFile.mkdirs()
                println()
            }
            if (!file.exists()) {
                var aaa = file.createNewFile()
                println()
            }

            var inputStream: InputStream? = null
            var outputStream: OutputStream = FileOutputStream(file)
            try {
                var fileReader = ByteArray(1024)
                var fileSize = it.contentLength()
                inputStream = it.byteStream()


                while(fileSize > 0) {

                    var read = inputStream.read(fileReader)
                    if (read == -1 ){
                        break
                    }
                    outputStream.write(fileReader,0,read)

                }

                outputStream.flush()
            } catch (e: IOException) {


            }  finally {

                if (inputStream != null) {

                    inputStream.close()
                }
                if (outputStream != null) {

                    outputStream.close()
                }
                mPlayerHandler.sendEmptyMessage(LRC_DOWNLOADED)
        }

        }


//        viewModel.lrcInfoData.observeForever {
//
//            val responseLrc = it
//            val lrcUrl: String
//            if (responseLrc != null) {
//                lrcUrl = responseLrc.lrcys_list?.get(0)?.lrclink ?: Null
//
//                if (mRequestLrc != null) {
//                    mLrcHandler.removeCallbacks(mRequestLrc)
//                }
//                mRequestLrc = RequestLrc(lrcUrl, getMusicEntity())
//                mLrcHandler.postDelayed(mRequestLrc, 70)
//            }
//        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_STICKY

    }
    override fun onBind(p0: Intent?): IBinder {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
    }

   fun handleCommandIntent(intent: Intent?) {

       when(intent?.action) {

           TOGGLE_PAUSE_ACTION -> {
               if (isPlaying) {
                   pause()
               } else {
                   play()
               }
           }
           NEXT_ACTION -> nextPlay()
           REPEAT_ACTION -> {
               cycleRepeat()
           }
           SHUFFLE_ACTION -> {
           }
           TRY_GET_TRACK_INFO -> {
               getLrc(mPlaylist[mPlayPos].mId)
           }
           STOP_ACTION -> {
               pause()
               seek(0)
               releaseServiceUiAndStop()
           }
       }
   }


    private fun cycleRepeat() {
        if (mRepeatMode == REPEAT_ALL) {
            setRepeatMode(REPEAT_CURRENT)
        } else {
            setRepeatMode(REPEAT_ALL)
        }
    }
    private fun releaseServiceUiAndStop() {
        if (isPlaying || mPlayerHandler.hasMessages(TRACK_ENDED)) {
            return
        }
        cancelNotification()
        if (!mServiceInUse) {
            stopSelf(mServiceStartId)
        }
    }
    private fun cancelNotification() {
        stopForeground(true)
        mNotificationManager.cancel(hashCode())
        mNotificationManager.cancel(mNotificationId)
        mNotificationPostTime = 0
        mNotifyMode = NOTIFY_MODE_NONE
    }

    private fun getLrc(id: Long) {
        val info = mPlayListInfo[id]
        val lrc = Environment.getExternalStorageDirectory().absolutePath + LRC_PATH
        var file = File(lrc)
        if (!file.exists()) {
            // 不存在就建立此目录
            file.mkdirs()
        }
        file = File("$lrc$id.lrc")
        if (!file.exists()) {
//            // 获取歌词

        } else {
            mPlayerHandler.sendEmptyMessage(LRC_DOWNLOADED)
        }
    }

    inner class RequestLrc(val url: String, private val musicEntity: MusicEntity?) : Runnable {

        override fun run() {

            if (url.isNotEmpty() && musicEntity != null) {

                viewModel.downloadPicFromNet(url)


            }
        }
    }

    fun sendUpdateBuffer(progress: Int) {

        val intent = Intent(BUFFER_UP)
        intent.putExtra("progress", progress)
        sendBroadcast(intent)

    }
    fun prepared() {

        isPlaying = true
        val intent = Intent(PLAY_STATE_CHANGED)
        sendBroadcast(intent)
    }


    fun loading(l: Boolean) {
        val intent = Intent(MUSIC_LOADING)
        intent.putExtra("isloading", l)
        sendBroadcast(intent)
    }

    private fun setIsPlaying(value: Boolean, notify: Boolean) {

    }
    fun stop(updateState: Boolean) {

        if (mPlayer.isInitialized) {
            mPlayer.stop()
        }
        mFileToPlay = Null
        if (updateState) {

            setIsPlaying(false,false)
        }
    }

     fun pause() {

         isPlaying = false
         mPlayer.pause()
    }

     fun play() {

         isPlaying = true
         mPlayer.start()

         val intent = Intent(PLAY_STATE_CHANGED)
         sendBroadcast(intent)
    }

     fun nextPlay() {

    }


     fun previous() {

    }

    fun playOnLine(songId: Long) {

        viewModel.getSong(songId)
    }

    fun openFile(path: String): Boolean {

         synchronized(this) {

             mFileToPlay = path
             mPlayer.setDataSource(mFileToPlay)
             if (mPlayer.isInitialized) {
                 mOpenFailedCounter = 0
                 return true
             }
            var trackName = getTrackName()
             if (TextUtils.isEmpty(trackName)) {
                 trackName = path

             }
             sendErrorMessage(trackName!!)
             stop(true)
             return true
         }
    }
    private fun getMusicEntity(): MusicEntity? {


        var entity: MusicEntity? = null
        if (mPlayPos in 0..(mPlaylist.size - 1)) {
            entity = mPlayListInfo[getAudioId()]
        }
        if (currentMusicEntity != null) {

            entity = currentMusicEntity
        }

        return entity
    }

    private fun sendErrorMessage(trackName: String) {

        val intent = Intent(TRACK_ERROR)
        intent.putExtra(TRACK_NAME, trackName)
        sendBroadcast(intent)
    }

        fun open(infos: Map<*, *>, list: LongArray, position: Int) {
        synchronized(this) {

        }

    }

     fun getArtistName(): String? {

         return currentMusicEntity?.artist
    }

     fun getTrackName(): String? {


        return currentMusicEntity?.musicName
    }

     fun getAlbumName(): String? {

        return currentMusicEntity?.albumName
    }

     fun getAlbumPath(): String? {

        return ""
    }

     fun getAlbumPic(): String? {

        return currentMusicEntity?.albumData
    }

     fun getAlbumId(): Long {
        return 0
    }

     fun getAlbumPathtAll(): Array<String?> {
        return Array<String?>(1) {""}
    }

     fun getPath(): String? {
        return null
    }

     fun getPlaylistInfo(): Map<Long, MusicEntity> {
        return mapOf()
    }

     fun getQueue(): LongArray {
        return LongArray(1)
    }

     fun getAudioId(): Long? {
        return currentMusicEntity?.audioId
    }

     fun getQueueSize(): Int {
        return  0
    }

     fun getQueuePosition(): Int {
        return  0
    }

     fun setQueuePosition(index: Int) {

    }

     fun removeTrack(id: Long): Int {
        return  -1
    }

     fun enqueue(list: LongArray, infos: Map<*, *>, action: Int) {
    }

     fun getRepeatMode(): Int {
        return  -1
    }

     fun setRepeatMode(repeatmode: Int) {

    }

     fun duration(): Long {
        return  mPlayer.duration()
    }

     fun position(): Long {
        return  mPlayer.position()
    }

     fun seek(position: Long): Long {

         var result: Long = -1
         if (mPlayer.isInitialized) {

             result = when {

                 position < 0 -> mPlayer.seek(0)
                 position > mPlayer.duration() -> mPlayer.seek(mPlayer.duration())
                 else -> mPlayer.seek(position)
             }
             notifyChange(POSITION_CHANGED)
         }
         return result    }

     fun isTrackLocal(): Boolean {
        return  true
    }

     fun secondPosition(): Int {
        return  0
    }

    fun getSecondPosition(): Int{

        return 0
    }
    fun getAlbumPathAll(): Array<String?> {

        return Array(1){""}
}
    fun notifyChange(what: String) {

        if (SEND_PROGRESS == what) {
            val intent = Intent(what)
            intent.putExtra("position", position())
            intent.putExtra("duration", duration())

            sendBroadcast(intent)
            return
        }
        if (what == POSITION_CHANGED) {
            return
        }

        val intent = Intent(what)
        intent.putExtra("id", getAudioId())
        intent.putExtra("artist", getArtistName())
        intent.putExtra("album", getAlbumName())
        intent.putExtra("track", getTrackName())
        intent.putExtra("playing", isPlaying)
        intent.putExtra("albumuri", getAlbumPath())
        intent.putExtra("islocal", isTrackLocal())
        sendBroadcast(intent)


        val musicIntent = Intent(intent)
        musicIntent.action = what.replace(TIMBER_PACKAGE_NAME, MUSIC_PACKAGE_NAME)
        sendBroadcast(musicIntent)

        if (what == META_CHANGED) {
            mRecentStore!!.addSongId(getAudioId()!!)
            currentMusicEntity = getMusicEntity()
        } else if (what == QUEUE_CHANGED) {
//            val intent1 = Intent("com.past.music.emptyplaylist")
//            intent.putExtra("showorhide", "show")
//            sendBroadcast(intent1)
//            saveQueue(true)
//            if (isPlaying) {
//                if (mNextPlayPos >= 0 && mNextPlayPos < mPlaylist.size) {
//                    setNextTrack(mNextPlayPos)
//                } else {
//                    setNextTrack()
//                }
//            }
        } else {
            saveQueue(false)
        }
        if (what == PLAY_STATE_CHANGED) {
            updateNotification()
        }
    }
    private fun updateNotification() {

        val notifyMode: Int = if (isPlaying) {
            NOTIFY_MODE_FOREGROUND
        } else {
            NOTIFY_MODE_NONE
        }
        if (notifyMode == mNotifyMode) {
            mNotificationManager.notify(mNotificationId, getNotification())
        } else {
            if (notifyMode == NOTIFY_MODE_FOREGROUND) {
                startForeground(mNotificationId, getNotification())
            } else {
                mNotificationManager.notify(mNotificationId, getNotification())
            }
        }
        mNotifyMode = notifyMode
    }

    private fun getNotification(): Notification {

//        val remoteViews = RemoteViews(this.packageName, R.layout.remote_view)
//        val pauseFlag = 0x1
//        val nextFlag = 0x2
//        val stopFlag = 0x3
//        val albumName = getAlbumName()
//        val artistName = getArtistName()
//        val isPlaying = isPlaying
//
//        val text = if (TextUtils.isEmpty(albumName)) artistName else "$artistName - $albumName"
//        remoteViews.setTextViewText(R.id.title, getTrackName())
//        remoteViews.setTextViewText(R.id.text, text)
//
//        // 此处action不能是一样的 如果一样的 接受的flag参数只是第一个设置的值
//        val pauseIntent = Intent(TOGGLE_PAUSE_ACTION)
//        pauseIntent.putExtra("FLAG", pauseFlag)
//        val pausePIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, 0)
//        remoteViews.setImageViewResource(R.id.img_play, if (isPlaying) R.drawable.icon_remote_pause else R.drawable.icon_remote_play)
//        remoteViews.setOnClickPendingIntent(R.id.img_play, pausePIntent)
//
//        val nextIntent = Intent(NEXT_ACTION)
//        nextIntent.putExtra("FLAG", nextFlag)
//        val nextPIntent = PendingIntent.getBroadcast(this, 0, nextIntent, 0)
//        remoteViews.setOnClickPendingIntent(R.id.img_next_play, nextPIntent)
//
//        val preIntent = Intent(STOP_ACTION)
//        preIntent.putExtra("FLAG", stopFlag)
//        val prePIntent = PendingIntent.getBroadcast(this, 0, preIntent, 0)
//        remoteViews.setOnClickPendingIntent(R.id.img_cancel, prePIntent)
//
//        val mMainIntent = Intent(this, MainActivity::class.java)
//        val mainIntent = PendingIntent.getActivity(this, 0, mMainIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        if (mNotificationPostTime == 0L) {
//            mNotificationPostTime = System.currentTimeMillis()
//        }
        var mNotification: Notification? = null
//        if (mNotification == null) {
//            val builder = NotificationCompat.Builder(this, APP_NAME)
//                .setContent(remoteViews)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentIntent(mainIntent)
//                .setWhen(mNotificationPostTime)
//            mNotification = builder.build()
//        } else {
//            mNotification.contentView = remoteViews
//        }
        return mNotification!!
    }

    private fun saveQueue(full: Boolean) {
        val editor = mPreferences!!.edit()
        if (full) {
            if (mPlayListInfo.size > 0) {
                val temp = Gson().toJson(mPlayListInfo)
                try {
                    val file = File(cacheDir.absolutePath + "playlist")
                    val ra = RandomAccessFile(file, "rws")
                    ra.write(temp.toByteArray())
                    ra.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
//            editor.putInt("cardid", mCardId)
        }
        editor.putInt("curpos", mPlayPos)
        if (mPlayer.isInitialized) {
            editor.putLong("seekpos", mPlayer.position())
        }
        editor.putInt("repeatmode", mRepeatMode)
        editor.apply()
    }
    private class MusicPlayerHandler constructor(service: MediaService, looper: Looper) : Handler(looper) {

        private val mService: WeakReference<MediaService> = WeakReference(service)

        override fun handleMessage(msg: Message?) {

        val service = mService.get() ?: return

            service.run {

                when(msg?.what) {
                    TRACK_WENT_TO_NEXT -> {

                    }
                    TRACK_ENDED -> if (mRepeatMode == REPEAT_CURRENT) {
                        seek(0)
                        play()
                    } else {
                        nextPlay()
                    }
                    LRC_DOWNLOADED -> notifyChange(LRC_UPDATED)

                    LRC_DOWNLOADED -> {

                        var intent = Intent(LRC_UPDATED)
                        sendBroadcast(intent)
                    }
                }
            }

        }
    }

    }