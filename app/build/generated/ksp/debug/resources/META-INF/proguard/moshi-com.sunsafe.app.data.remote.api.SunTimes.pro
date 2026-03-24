-if class com.sunsafe.app.data.remote.api.SunTimes
-keepnames class com.sunsafe.app.data.remote.api.SunTimes
-if class com.sunsafe.app.data.remote.api.SunTimes
-keep class com.sunsafe.app.data.remote.api.SunTimesJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
