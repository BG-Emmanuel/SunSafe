-if class com.sunsafe.app.data.remote.api.SunInfo
-keepnames class com.sunsafe.app.data.remote.api.SunInfo
-if class com.sunsafe.app.data.remote.api.SunInfo
-keep class com.sunsafe.app.data.remote.api.SunInfoJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
