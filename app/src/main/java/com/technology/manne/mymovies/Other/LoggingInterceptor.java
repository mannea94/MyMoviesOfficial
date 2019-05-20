package com.technology.manne.mymovies.Other;

import android.content.Context;
import android.util.Log;

import com.technology.manne.mymovies.Model.RequestToken;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by manne on 18.1.2018.
 */

public class LoggingInterceptor implements Interceptor {
    private Context activity;
    private RequestToken token;
    private boolean logout = false;

    public LoggingInterceptor(Context activity, RequestToken token) {
        this.activity = activity;
        this.token = token;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        String access_token = null;
        try {
            access_token = token.request_token;
        } catch (NullPointerException e) {
        }

        request = request.newBuilder().addHeader("Authorization", "Bearer " + ((access_token != null) ? access_token : "")).build();

        long t1 = System.nanoTime();

        if ((request.body() != null && request.body().contentLength() < 2048) || request.body() == null)
            Log.d("Retrofit", String.format("Sending request %s on %s%n%s", request.url(), chain
                    .connection(), request.headers()) + " Params " + bodyToString(request));

        Response response = chain.proceed(request);

        String responseBodyString = response.body().string();
        long t2 = System.nanoTime();
        Log.d("Retrofit", String.format("Received response for %s in %.1fms%n%s", response.request
                ().url(), (t2 - t1) / 1e6d, response.headers()) + "Response code:" + response.code() + "\n" + "Body " + responseBodyString);



        return response.newBuilder().body(ResponseBody.create(response.body().contentType(),
                responseBodyString)).build();
    }

    private String bodyToString(final Request request) {
        try {
            final Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        } catch (NullPointerException e) {
            return "did not work nullPointer";
        } catch (OutOfMemoryError e) {
            return "OutOfMemoryError ";
        }
    }
}

