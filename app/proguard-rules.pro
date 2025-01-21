# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# saudi tourism
-keep class sa.sauditourism.employee.** { *; }
-keep interface kotlin.Metadata { *; }
-keepattributes RuntimeVisibleAnnotations

-if class **$Companion extends **
-keep class <2>
-if class **$Companion implements **
-keep class <2>

## Adjust
-keep class com.adjust.sdk.** { *; }
-keep class com.google.android.gms.common.ConnectionResult {
    int SUCCESS;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
    com.google.android.gms.ads.identifier.AdvertisingIdClient$Info getAdvertisingIdInfo(android.content.Context);
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
    java.lang.String getId();
    boolean isLimitAdTrackingEnabled();
}
-keep public class com.android.installreferrer.** { *; }
## Hilt
-keepnames class dagger.**
-keep class dagger.hilt.android.internal.** {*;}

-keep public class com.payment.paymentsdk.** { *; }


-keep,allowobfuscation @interface com.google.gson.annotations.SerializedName
-keep class sa.sauditourism.repo.trip.data.** { *; }
-keep class sa.sauditourism.repo.trip.model.remote.response.** { *; }
-keep class sa.sauditourism.repo.trip.model.local.** { *; }

# Huawei
-keep class com.huawei.hms.** { *; }
-dontwarn com.huawei.hms.**
-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.huawei.hianalytics.** { *; }
-keep class com.huawei.updatesdk.** { *; }
-keep class com.huawei.agconnect.**{*;}
-keep interface com.huawei.agconnect.** { *; }

# Bouncy Castle
-keep class org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**

# Conscrypt
-keep class org.conscrypt.** { *; }
-dontwarn org.conscrypt.**

# OpenJSSE
-keep class org.openjsse.** { *; }
-dontwarn org.openjsse.**

# telephony
-keep class com.huawei.android.telephony.** { *; }
-dontwarn com.huawei.android.telephony.**

# hianalytics
-keep class  com.huawei.hianalytics.** { *; }
-dontwarn com.huawei.hianalytics.**

-keep class androidx.**
-dontwarn androidx.**

-keep class retrofit2.**
-dontwarn retrofit2.**

-keep class java.**
-dontwarn java.**

-keep class javax.**
-dontwarn javax.**

-keep class kotlinx.**
-dontwarn kotlinx.**

-keep class kotlin.**
-dontwarn kotlin.**

-keep class sa.sauditourism.employee.models.**
-dontwarn sa.sauditourism.employee.models.**

-keep class com.google.**
-dontwarn com.google.**

# This is generated automatically by the Android Gradle plugin.
-dontwarn android.os.ServiceManager
-dontwarn com.bun.miitmdid.core.MdidSdkHelper
-dontwarn com.bun.miitmdid.interfaces.IIdentifierListener
-dontwarn com.bun.miitmdid.interfaces.IdSupplier
-dontwarn com.google.firebase.iid.FirebaseInstanceId
-dontwarn com.google.firebase.iid.InstanceIdResult
-dontwarn com.huawei.hms.ads.identifier.AdvertisingIdClient$Info
-dontwarn com.huawei.hms.ads.identifier.AdvertisingIdClient
-dontwarn com.tencent.android.tpush.otherpush.OtherPushClient

-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

-keepclassmembers enum * {
  @com.google.gson.annotations.SerializedName <fields>;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep class org.objenesis.** { *; }
