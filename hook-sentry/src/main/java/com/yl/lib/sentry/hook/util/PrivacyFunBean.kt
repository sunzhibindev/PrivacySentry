package com.yl.lib.sentry.hook.util

import android.text.TextUtils
import java.lang.StringBuilder

/**
 * @author yulun
 * @sinice 2021-11-19 15:24
 */
class PrivacyFunBean {

    var appendTime: String? = null

    /**
     * 类似于我们正常理解中的别名，比如imsi在系统里的函数名并不是imsi
     */
    var funAlias: String? = null
    var funName: String? = null
    var stackTraces: String? = null
    var count = 0


    constructor(alias: String?, funName: String?, stackTrace: String?, count: Int) {
        appendTime = PrivacyUtil.Util.formatTime(System.currentTimeMillis(), "MM-dd HH:mm:ss.SSS")
        this.funAlias = alias
        this.funName = funName
        this.stackTraces = trimTrace(stackTrace)
        this.count = count
    }

    constructor(alias: String?, funName: String?) {
        this.funAlias = alias
        this.funName = funName
    }

    fun addSelf() {
        count++
    }

    fun buildStackTrace(): String {
        if (stackTraces == null || stackTraces?.isEmpty() != false) {
            return ""
        }
        return stackTraces ?: ""
    }

    // 裁剪掉部分冗余重复的trace
    private fun trimTrace(stackStrace: String?): String? {
        if (TextUtils.isEmpty(stackStrace))
            return ""
        var sArray = stackStrace?.split("\n")
        var delIndex = sArray?.indexOfFirst { it.contains("java.lang.reflect.Proxy.invoke") }
        if (delIndex != null && delIndex <= 0) {
            return stackStrace
        }
        return sArray?.subList(delIndex!!+1,sArray.size)?.joinToString(separator = "\n")
    }
}