package eu.hithredin.spsdk.query.ion;

import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;

import java.lang.ref.WeakReference;

import eu.hithredin.spsdk.data.DeviceData;
import eu.hithredin.spsdk.query.QueryCallback;
import eu.hithredin.spsdk.query.ResultInfo;

/**
 * Created by benoit on 2/10/16.
 * TODO BEAN IMMEDIATE FAST CACHE OVER ION
 */
public abstract class IonCallback<T, E> {

    protected WeakReference<QueryCallback<T, E>> callbackRef;
    protected int queryId;
    protected Object tag;


    /**
     * Set the callback to receive the query result
     *
     * @param callback
     * @return
     */
    public IonCallback callback(QueryCallback<T, E> callback) {
        callbackRef = new WeakReference<>(callback);
        return this;
    }


    public IonCallback id(int id) {
        this.queryId = id;
        return this;
    }

    /**
     * An object sent back with the query callback
     *
     * @param tag
     * @return
     */
    public IonCallback tag(Object tag) {
        this.tag = tag;
        return this;
    }

    protected ResultInfo getResultInfo(Response r) {
        ResultInfo ri = new ResultInfo();
        ri.setTag(tag);

        if(r!=null && r.getHeaders() != null) {
            ri.setHttpCode(r.getHeaders().code());
        }
        return ri;
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

    /**
     * Start the query
     *
     * @param ion
     */
    public abstract void launch(Builders.Any.B ion);

    public void launch(String url){
       launch(Ion.with(DeviceData.ctx()).load(url));
    }

    public void launch(int id, QueryCallback callback, Builders.Any.B ion){
        callback(callback).id(id).launch(ion);
    }

}
