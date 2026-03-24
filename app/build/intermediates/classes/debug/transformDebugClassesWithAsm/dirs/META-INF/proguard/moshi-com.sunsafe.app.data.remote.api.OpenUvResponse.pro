-if class com.sunsafe.app.data.remote.api.OpenUvResponse
-keepnames class com.sunsafe.app.data.remote.api.OpenUvResponse
-if class com.sunsafe.app.data.remote.api.OpenUvResponse
-keep class com.sunsafe.app.data.remote.api.OpenUvResponseJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
