# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
 #优化  不优化输入的类文件
-dontoptimize
 #预校验
-dontpreverify
 #混淆时是否记录日志
-verbose
#保护泛型
-keepattributes Signature
#保护注解
-keepattributes *Annotation*
# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
	native <methods>;
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
	public <init>(android.content.Context, android.util.AttributeSet);
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
	public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
	public void *(android.view.View);
}
#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持Jpush不被混淆
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

#WebView中用到JS
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

#保持okhttp不被混淆
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-dontwarn okio.**

#百度地图
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**

#umeng统计
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#bugly
-keep public class com.tencent.bugly.**{*;}

#zxing
-dontwarn com.google.zxing.**
-keep class com.google.zxing.** {*; }

#-dontwarn和-keep 结合使用，意思是保持com.xx.bbb.**这个包里面的所有类和所有方法而不混淆，
#接着还叫ProGuard不要警告找不到com.xx.bbb.**这个包里面的类的相关引用。
#掌贝POS
-dontwarn com.yunnex.printlib.**
-dontwarn com.nostra13.universalimageloader.core.**
-keep class com.yunnex.printlib.** {*;}
-keep class com.nostra13.universalimageloader.core.** {*;}

#facebook图片控件
-keep class com.facebook.imagepipeline.** { *;}

#Gson specific classes
-keep class sun.misc.Unsafe { *; }
#实体类
-keep class com.wjika.cardstore.network.entities.** { *;}

-dontwarn u.aly.**
-keep class u.aly.** { *; }