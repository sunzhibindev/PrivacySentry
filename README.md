# PrivacySentry
    android隐私合规检测

## 更新日志
    2021-12-02
        1. 支持多进程
        2. 日志加上时间戳，方便阅读
        3. 优化文件分时段写入
        4. pms增加部分hook方法

## TODO
1. 部分无法hook的函数，插桩实现
2. 每个Context的实例就持有了一个PMS代理对象的引用,理论上只要hook application的就够了，这里用asm拦截比较好


## 如何使用

```
    1. 在根目录的build.gralde下添加
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```



```
    2. 在项目中的build.gralde下添加
      implementation 'com.github.allenymt:PrivacySentry:0.0.7'
```

```
    3. 初始化方法最好在attachBaseContext中第一个调用！！！
```

```
    4.1 简易版初始化
    在代码中调用，越早越好，建议在application中调用
    kotlin:PrivacySentry.Privacy.init(this)
    java:PrivacySentry.Privacy.INSTANCE.init(this);
```


```
    4.2 完成功能的初始化
     // 完整版配置
        var builder = PrivacySentryBuilder()
            // 自定义文件结果的输出名
            .configResultFileName("demo_test")
            //自定义检测时间，也支持主动停止检测 PrivacySentry.Privacy.stopWatch()
            .configWatchTime(5 * 60 * 1000)
            // 文件输出后的回调
            .configResultCallBack(object : PrivacyResultCallBack {
                override fun onResultCallBack(filePath: String) {
                    PrivacyLog.i("result file patch is $filePath")
                }
            })
        PrivacySentry.Privacy.init(this, PrivacySentry.Privacy.defaultConfigHookBuilder(builder))
        
        
        java
         // 完整版配置
        PrivacySentryBuilder builder = new PrivacySentryBuilder()
                // 自定义文件结果的输出名
                .configResultFileName("buyer_privacy")
                //自定义检测时间，也支持主动停止检测 PrivacySentry.Privacy.stopWatch()
                .configWatchTime(30 * 1000)
                // 文件输出后的回调
                .configResultCallBack(new PrivacyResultCallBack() {

                    @Override
                    public void onResultCallBack(@NonNull String s) {

                    }
                });
        PrivacySentry.Privacy.INSTANCE.init(this, PrivacySentry.Privacy.INSTANCE.defaultConfigHookBuilder(builder));
```


```
    4.3 在隐私协议确认的时候调用，这一步非常重要！，一定要加
    kotlin:PrivacySentry.Privacy.updatePrivacyShow()
    java:PrivacySentry.Privacy.INSTANCE.updatePrivacyShow();
```


```
    5 支持多进程，多进程产出的文件名前缀默认增加进程名
```



## 隐私方法调用结果产出
-     默认拦截隐私方法时间为3分钟，支持自定义设置时间。
-     排查结果可参考目录下的demo_result.xls，排查结果支持两个维度查看，第一是结合隐私协议的展示时机和敏感方法的调用时机，第二是统计所有敏感函数的调用次数
-     排查结果可观察日志，结果文件会在 /storage/emulated/0/Android/data/yourPackgeName/files/privacy/yourFileName.xls，需要手动执行下adb pull

## 基本原理
-     一期是运行期基于动态代理hook系统关键函数实现，二期计划是编译期代码插桩实现
-     为什么不用xposed等框架？ 因为想做本地自动化定期排查，第三方hook框架外部依赖性太大
-     为什么不搞基于lint的排查方式？ 工信部对于运行期 敏感函数的调用时机和次数都有限制，代码扫描解决不了这些问题


## 支持的hook函数列表

敏感函数 | 函数说明 | 归属的系统服务
---|---|---
getRunningTasks getRunningAppProcesses | 获取运行中的进程，多进程的APP中一般都有调用 | ActivityManagerService(AMS)
getInstalledPackages queryIntentActivities getLeanbackLaunchIntentForPackage getInstalledPackagesAsUser queryIntentActivitiesAsUser queryIntentActivityOptions | 获取手机上已安装的APP  | PackageManager(PMS)
getSimSerialNumber getDeviceId getSubscriberId getDeviceId | 设备和硬件标识  | TelephonyManager(TMS)
getPrimaryClip | 剪贴板内容，Android12开始，读取剪贴板会通知用户，因此这里也加一个 | ClipboardManager(CMS)





## 结语
    整体代码很简单，有问题可以直接提~
    不要把代码带到线上！！！
