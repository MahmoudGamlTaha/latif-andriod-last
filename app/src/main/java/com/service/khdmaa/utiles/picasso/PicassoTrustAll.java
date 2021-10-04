package com.service.khdmaa.utiles.picasso;

import android.content.Context;
import android.net.Uri;
import android.util.Log;


import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.Collections;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public class PicassoTrustAll {

    private static String LOG_TAG = PicassoTrustAll.class.getSimpleName();
    private static boolean hasCustomPicassoSingletonInstanceSet;

    public static Picasso with(Context context) {

        if (hasCustomPicassoSingletonInstanceSet)
            return Picasso.get();

        try {
            Picasso.setSingletonInstance(null);
        } catch (IllegalStateException e) {
            Log.w(LOG_TAG, "-> Default singleton instance already present" +
                    " so CustomPicasso singleton cannot be set. Use CustomPicasso.getNewInstance() now.");
            return Picasso.get();
        }

        Picasso picasso = new Picasso.Builder(context).
                downloader(new OkHttp3Downloader(context))
                .build();

        Picasso.setSingletonInstance(picasso);
        Log.w(LOG_TAG, "-> CustomPicasso singleton set to Picasso singleton." +
                " In case if you need Picasso singleton in future then use Picasso.Builder()");
        hasCustomPicassoSingletonInstanceSet = true;

        return picasso;
    }

    public static Picasso getNewInstance(Context context) {

        Log.w(LOG_TAG, "-> Do not forget to call customPicasso.shutdown()" +
                " to avoid memory leak");

        return new Picasso.Builder(context).
                downloader(new OkHttp3Downloader(context))
                .build();
    }
}
