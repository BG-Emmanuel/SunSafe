-if class com.sunsafe.app.data.remote.api.ForecastItem
-keepnames class com.sunsafe.app.data.remote.api.ForecastItem
-if class com.sunsafe.app.data.remote.api.ForecastItem
-keep class com.sunsafe.app.data.remote.api.ForecastItemJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
