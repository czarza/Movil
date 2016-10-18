# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\MyAndroid\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
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
-keepclassmembers class com.mocoo.hang.rtprinter.driver.HsBluetoothPrintDriver$ConnectThread{
    com.mocoo.hang.rtprinter.driver.HsBluetoothPrintDriver$ConnectThread(***);
}
-keepclassmembers class com.mocoo.hang.rtprinter.driver.HsBluetoothPrintDriver$AcceptThread{
    com.mocoo.hang.rtprinter.driver.HsBluetoothPrintDriver$AcceptThread();
}
-keepclassmembers class com.mocoo.hang.rtprinter.driver.LabelBluetoothPrintDriver$ConnectThread{
    com.mocoo.hang.rtprinter.driver.LabelBluetoothPrintDriver$ConnectThread(***);
}
-keepclassmembers class com.mocoo.hang.rtprinter.driver.LabelBluetoothPrintDriver$AcceptThread{
    com.mocoo.hang.rtprinter.driver.LabelBluetoothPrintDriver$AcceptThread();
}

-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-keep class com.squareup.picasso.**
-dontwarn com.squareup.picasso.**
-keep class okio.**
-dontwarn okio.**
-keep class retrofit2.Platform$Java8
-dontwarn retrofit2.Platform$Java8
-keep class sun.misc.Unsafe
-dontwarn sun.misc.Unsafe
-keep class org.w3c.dom.bootstrap.DOMImplementationRegistry
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry
-keep zj.com.**
-dontwarn zj.com.**
-keepclasseswithmembernames class * {
    native <methods>;
}