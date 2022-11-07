package com.ayxls.library_epager.utils.http_sender;

import android.app.Application;
import okhttp3.OkHttpClient;
import rxhttp.RxHttpPlugins;
import rxhttp.wrapper.cookie.CookieStore;
import rxhttp.wrapper.ssl.HttpsUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class RxHttpManager {

    public static void init(Application application,boolean isDebug) {
        OkHttpClient client = getOkHttpClient(application);
        RxHttpPlugins.init(client)
                //调试模式/分段打印/json数据缩进空间
                .setDebug(isDebug,false,2)
                .setOnParamAssembly(param -> {
//                    //添加公共参数
//                    param.add("version", "1.0.0");
                });
    }

    private static OkHttpClient getOkHttpClient(Application application) {
        File file = new File(application.getExternalCacheDir(), "RxHttpCache");
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        return new OkHttpClient.Builder()
                .cookieJar(new CookieStore(file))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)//添加信任证书
                .hostnameVerifier((hostname, session) -> true)//忽略host验证
                .build();
    }
}
