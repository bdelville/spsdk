/*
 * Copyright (C) 2014 Modified by Sedona Paris
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hithredin.spsdk.query;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

/**
 * Build a HTTP request supporting POST, GET, PUT, POST and multiple kind of data parsing
 *
 * @param <T> Succes object type
 * @param <E> Error Object type
 */
public class HttpRequestBuilder<T, E> extends RequestBuilder<T,E> {

    private static final String LOG_TAG = HttpRequestBuilder.class.getSimpleName();

    //protected String postParamRaw;
    protected ByteArrayOutputStream postParamRaw;
    protected String contentType;
    protected static String multipartBoundary = "AaB03xBounDaRy";
    protected Map<String, String> postParamUrlEncoded;

    //TODO Cache, all parser type, post/get/put/delete,

    @SuppressLint("NewApi")
    public static void clearAllCookies() {
        //TODO
    }


    /**
     * Send data with the query as it is (no formatting)
     *
     * @param postParamRaw
     * @return
     */
    public HttpRequestBuilder postParamRaw(String postParamRaw) {
        try {
            this.postParamRaw = new ByteArrayOutputStream();
            this.postParamRaw.write(postParamRaw.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    private boolean isMultipart = false;


    public HttpRequestBuilder postAddMultipart(String headerText, byte[] postParamRaw) {
        isMultipart = true;
        if (contentType == null) {
            contentType = "multipart/form-data, boundary=" + multipartBoundary;
        }
        if (this.postParamRaw == null) {
            this.postParamRaw = new ByteArrayOutputStream();
        }

        try {
            this.postParamRaw.write(("\r\n--" + multipartBoundary + "\r\n").getBytes());
            this.postParamRaw.write((headerText + "\r\n\r\n").getBytes());
            this.postParamRaw.write(postParamRaw);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public HttpRequestBuilder postAddMultipartForm(String name, String value) {
        if (name == null || value == null) {
            return this;
        }
        isMultipart = true;
        if (contentType == null) {
            contentType = "multipart/form-data, boundary=" + multipartBoundary;
        }
        if (this.postParamRaw == null) {
            this.postParamRaw = new ByteArrayOutputStream();
        }

        try {
            this.postParamRaw.write(("\r\n--" + multipartBoundary + "\r\n").getBytes());
            this.postParamRaw.write(("content-disposition: form-data; name=" + name + "\r\n").getBytes());
            this.postParamRaw.write(("\r\n" + value).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Force a content-Type
     *
     * @param contentType
     * @return
     */
    public HttpRequestBuilder contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Data to send with the query as UrlEncoded form
     *
     * @param postParamUrlEncoded
     * @return
     */
    public HttpRequestBuilder postParamUrlEncoded(Map<String, String> postParamUrlEncoded) {
        this.postParamUrlEncoded = postParamUrlEncoded;
        return this;
    }


    /**
     * Json data to send with the query
     *
     * @param data
     * @return
     */
    public HttpRequestBuilder postJson(Object data) {
        contentType("application/json");
        return postParamRaw(gson.toJson(data));
    }

    /**
     * Create a request with mandatory parameters
     */
    public HttpRequestBuilder() {

    }

    /**
     * Do not use except if strictly required
     *
     * @param context
     */
    public static void trustAllCertificate(Context context) {
        Log.e(LOG_TAG, "trustAllCertificate Security warning: All ssl certificates will be trusted!!!");

        try {
            //TODO
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * If it needs a SSL certificat
     *
     * @param ctx
     * @param certName
     * @return
     */
    public static SSLSocketFactory loadCA(Context ctx, String certName) {
        if (certName != null) {
           //TODO
        }
        return null;
    }

    @Override
    public void loadAsync() {

    }
}