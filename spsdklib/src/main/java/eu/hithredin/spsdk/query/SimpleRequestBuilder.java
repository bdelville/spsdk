package eu.hithredin.spsdk.query;

import com.google.gson.Gson;

import java.lang.ref.WeakReference;

/**
 * Created by benoit on 1/8/16.
 */
public abstract class SimpleRequestBuilder<T,E> {

    private static final String LOG_TAG = RequestBuilder.class.getSimpleName();

    protected WeakReference<QueryCallback<T, E>> callbackRef;
    protected int queryId;
    private Object tag;


    /**
     * Set the callback to receive the query result
     *
     * @param callback
     * @return
     */
    public SimpleRequestBuilder callback(QueryCallback<T, E> callback) {
        callbackRef = new WeakReference<QueryCallback<T, E>>(callback);
        return this;
    }


    public SimpleRequestBuilder id(int id){
        this.queryId = id;
        return this;
    }


    /**
     * An object sent back with the query callback
     *
     * @param tag
     * @return
     */
    public SimpleRequestBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    /**
     * Timeout of the request
     *
     * @param timeout
     * @return
     */
    public SimpleRequestBuilder timeout(int timeout) {
        //TODO
        return this;
    }


    protected void sendCallback(ResultInfo queryResultInfo, T dataParsed, E dataError) {
        if (callbackRef != null) {
            queryResultInfo.setTag(tag);
            QueryCallback<T, E> callback = callbackRef.get();
            if (callback == null) {
                return;
            }
            callback.onQueryFinished(queryId, queryResultInfo, dataParsed, dataError);
        }
    }

    public abstract void loadAsync();
}
