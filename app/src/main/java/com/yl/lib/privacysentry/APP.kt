package com.yl.lib.privacysentry

import android.app.Application
import android.content.Context
import com.yl.lib.sentry.hook.PrivacyResultCallBack
import com.yl.lib.sentry.hook.PrivacySentry
import com.yl.lib.sentry.hook.PrivacySentryBuilder
import com.yl.lib.sentry.hook.printer.DefaultLogPrint
import com.yl.lib.sentry.hook.util.PrivacyLog

/**
 * @author yulun
 * @sinice 2021-11-19 10:20
 */
class APP : Application() {
    override fun onCreate() {
        super.onCreate()

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        initPrivacy()
    }

    private fun initPrivacy(){

        // 完整版配置
        var builder = PrivacySentryBuilder()
            // 自定义文件结果的输出名
            .configResultFileName("demo_result")
            //自定义检测时间，也支持主动停止检测 PrivacySentry.Privacy.stopWatch()
            .configWatchTime(10 * 60 * 1000)
            // 文件输出后的回调
            .configResultCallBack(object : PrivacyResultCallBack {
                override fun onResultCallBack(filePath: String) {
                    PrivacyLog.i("result file patch is $filePath")
                }
            })
        // 添加默认结果输出，包含log输出和文件输出
        PrivacySentry.Privacy.init(this, PrivacySentry.Privacy.defaultConfigHookBuilder(builder))

        // 简易版配置
//        PrivacySentry.Privacy.init(this)
    }
}