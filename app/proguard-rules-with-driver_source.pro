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

-keep class com.mocoo.hang.swipeback.**{*;}
-dontwarn com.mocoo.hang.swipeback.**
#-keep class com.mocoo.hang.swipeback.app.**{*;}

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
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.**
-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-keepclasseswithmembernames class * {
    native <methods>;
}
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8