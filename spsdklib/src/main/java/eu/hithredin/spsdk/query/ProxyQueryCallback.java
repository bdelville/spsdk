package eu.hithredin.spsdk.query;

import java.lang.ref.WeakReference;

import eu.hithredin.spsdk.common.trick.ReferenceKeeper;

/**
 * A callback proxy used to modify the data before sending it to the caller.
 * Used when the Parsing Models are not coherents with the View models and need no be changed
 * @param <T> Data object for mwebservice
 * @param <E> Error object from webservice
 * @param <V> Data object converted
 * @param <W> Error object converted
 */
public abstract class ProxyQueryCallback<T,E,V,W> implements QueryCallback<T,E>,ReferenceKeeper.KeepedReference {

    protected WeakReference<QueryCallback<V,W>> callbackRef;

    public ProxyQueryCallback(QueryCallback<V,W> callback) {
        callbackRef = new WeakReference<>(callback);
        ReferenceKeeper.get().keep(this, callback);
    }

    @Override
    public void onQueryFinished(ResultInfo queryInfo, T data, E error) {
        ReferenceKeeper.get().forget(this);
    }

    protected void sendParentCallback(ResultInfo queryInfo, V data, W error){
        if (callbackRef.get() != null) {
            callbackRef.get().onQueryFinished(queryInfo, data, error);
        }
    }
}
