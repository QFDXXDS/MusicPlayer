package com.xxds.musicplayer.modules.playing.entities

import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.text.format.DateUtils
import android.view.Gravity
import java.io.File
import java.util.regex.Pattern

class LrcEntry(val time: Long, val text: String): Comparable<LrcEntry> {

    companion object {

        const val GRAVITY_LEFT = 1
        const val GRAVITY_CENTER = 2
        const val GRAVITY_RIGHT = 3
    }
    var offset = Float.MIN_VALUE
    lateinit var staticLayout: StaticLayout

    fun init(paint: TextPaint, width: Int,gravity: Int) {

        val align: Layout.Alignment = when(gravity) {

            GRAVITY_LEFT -> Layout.Alignment.ALIGN_NORMAL
            GRAVITY_CENTER -> Layout.Alignment.ALIGN_CENTER
            GRAVITY_RIGHT -> Layout.Alignment.ALIGN_OPPOSITE

            else -> Layout.Alignment.ALIGN_NORMAL
        }


        staticLayout = StaticLayout(text, paint, width, align, 1f, 0f, false)

    }

//    获取staticLayout的高度
    fun getHeight(): Int {
        return staticLayout.height
    }

//    数组sort()会执行该方法， *将此对象与指定的order对象进行比较。如果该对象相等，则返回零
//*对于指定的[other]对象，如果它小于[other]，则为负数，或者为正数
//如果它大于[其他]。
    override fun compareTo(other: LrcEntry): Int {

        return (time - other.time).toInt()
    }
}



fun parseLrc(lrcFile: File?): List<LrcEntry>? {

    if (lrcFile == null || !lrcFile.exists()) {

        return null
    }
    val entryList = ArrayList<LrcEntry>()

    lrcFile.forEachLine { line ->
//        解析每行
        val list = parseLine(line)
        if (list != null && !list.isEmpty()) {
            entryList.addAll(list)

        }

        entryList.sort()
    }


    return entryList

}

fun parseLrc(lrxText: String): List<LrcEntry>? {

    if (TextUtils.isEmpty(lrxText)) {

        return null
    }
    val entryList = ArrayList<LrcEntry>()

    val array = lrxText.split("\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    for (line in array) {

        val list = parseLine(line)

//        list 可能为空（因为有些为空）
        if (list != null && !list.isEmpty()) {

            entryList.addAll(list)
        }
     }
    entryList.sort()

    return entryList

}


private fun parseLine(line: String): List<LrcEntry>? {

//    [ti:至少还有你爱我]
//    [ar:龙梅子&王娜]
//    [al:至少还有你爱我]
//    [by:普天同乐]
//    [00:00.74]至少还有你爱我
//    [00:05.08]演唱：龙梅子&王娜
//    [00:08.96]作词：宋普照
//    [00:10.07]作曲：惊世杰
//    [00:12.15]编曲：牛子健
//    [00:15.90]和声：樊竹青
//    [00:18.88]录／混：周晓明
//    [00:21.36]制作人：陈晓龙
//    [00:29.98]这些年行走在水深火热

    var lineTemp = line
    if (TextUtils.isEmpty(lineTemp)) {

        return null
    }

    lineTemp = lineTemp.trim()
//regex：是一个正则表达式的字符串，
    val lineMatcher = Pattern.compile("((\\[\\d\\d:\\d\\d\\.\\d{2,3}\\])+)(.+)").matcher(lineTemp)
    if (!lineMatcher.matches()) {

        return null
    }

    val times = lineMatcher.group(1)
    val  text = lineMatcher.group(3)
    val entryList = ArrayList<LrcEntry>()


    val timeMatcher = Pattern.compile("\\[(\\d\\d):(\\d\\d)\\.(\\d){2,3}\\]").matcher(times)

    while (timeMatcher.find()) {

        val min = java.lang.Long.parseLong(timeMatcher.group(1))
        val sec = java.lang.Long.parseLong(timeMatcher.group(2))
        val mil = java.lang.Long.parseLong(timeMatcher.group(3))
//        总时间
        val time = min *DateUtils.MINUTE_IN_MILLIS + sec *DateUtils.SECOND_IN_MILLIS + if (mil >= 100L) mil else mil * 10
        entryList.add(LrcEntry(time,text))
    }
    return entryList



}
