package eu.hithredin.spsdk.query;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by benoit on 1/8/16.
 */
public abstract class BaseRequestBuilder<T, E> {

    private static final String LOG_TAG = BaseRequestBuilder.class.getSimpleName();

    protected WeakReference<QueryCallback<T, E>> callbackRef;
    protected int queryId;
    protected Object tag;
    protected ByteArrayOutputStream postParamRaw;
    protected String contentType;
    protected static String multipartBoundary = "AaB03xBounDaRy";
    protected Map<String, String> postParamUrlEncoded;
    protected boolean isMultipart = false;
    protected String url;
    protected int timeout;


    public BaseRequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    /**
     * Set the callback to receive the query result
     *
     * @param callback
     * @return
     */
    public BaseRequestBuilder callback(QueryCallback<T, E> callback) {
        callbackRef = new WeakReference<QueryCallback<T, E>>(callback);
        return this;
    }


    public BaseRequestBuilder id(int id) {
        this.queryId = id;
        return this;
    }


    /**
     * An object sent back with the query callback
     *
     * @param tag
     * @return
     */
    public BaseRequestBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    /**
     * Timeout of the request
     *
     * @param timeout
     * @return
     */
    public BaseRequestBuilder timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }


    protected void sendCallback(ResultInfo queryResultInfo, T dataParsed, E dataError) {
        if (callbackRef != null) {
            QueryCallback<T, E> callback = callbackRef.get();
            if (callback == null) {
                return;
            }

            queryResultInfo.tag = tag;
            queryResultInfo.idQuery = queryId;
            callback.onQueryFinished(queryResultInfo, dataParsed, dataError);
        }
    }

    /**
     * Send data with the query as it is (no formatting)
     *
     * @param postParamRaw
     * @return
     */
    public BaseRequestBuilder postParamRaw(String postParamRaw) {
        try {
            this.postParamRaw = new ByteArrayOutputStream();
            this.postParamRaw.write(postParamRaw.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }


    public BaseRequestBuilder postAddMultipart(String headerText, byte[] postParamRaw) {
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

    public BaseRequestBuilder postAddMultipartForm(String name, String value) {
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
    public BaseRequestBuilder contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Data to send with the query as UrlEncoded form
     *
     * @param postParamUrlEncoded
     * @return
     */
    public BaseRequestBuilder postParamUrlEncoded(Map<String, String> postParamUrlEncoded) {
        this.postParamUrlEncoded = postParamUrlEncoded;
        return this;
    }


    /**
     * Json data to send with the query
     *
     * @param data
     * @return
     */
    public BaseRequestBuilder postJson(Gson gson, Object data) {
        contentType("application/json");
        return postParamRaw(gson.toJson(data));
    }

    /**
     * Start the query
     */
    public void launch(LibQueryManager manager){
        manager.launch(this);
    }


    public int getQueryId() {
        return queryId;
    }

    public Object getTag() {
        return tag;
    }

    public ByteArrayOutputStream getPostParamRaw() {
        return postParamRaw;
    }

    public String getContentType() {
        return contentType;
    }

    public static String getMultipartBoundary() {
        return multipartBoundary;
    }

    public Map<String, String> getPostParamUrlEncoded() {
        return postParamUrlEncoded;
    }

    public boolean isMultipart() {
        return isMultipart;
    }

    public String getUrl() {
        return url;
    }

    public int getTimeout() {
        return timeout;
    }

    public abstract void parseResponse(Object data, ResultInfo infos);
    public abstract void parseError(Object data, ResultInfo infos);
}
