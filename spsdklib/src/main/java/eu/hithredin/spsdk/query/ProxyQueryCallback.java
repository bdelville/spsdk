package eu.hithredin.spsdk.query;

import java.lang.ref.WeakReference;

import eu.hithredin.spsdk.common.trick.ReferenceKeeper;

/**
 * A callback proxy used to modify the data before sending it to the caller.
 * Used when the Parsing Models are not coherents with the View models and need no be changed
 */
public abstract class ProxyQueryCallback<T,E,V,W> implements QueryCallback<T,E>,ReferenceKeeper.KeepedReference {

    protected WeakReference<QueryCallback<V,W>> callbackRef;

    public ProxyQueryCallback(QueryCallback<V,W> callback) {
        callbackRef = new WeakReference<>(callback);
        ReferenceKeeper.get().keep(this);//ReferenceKeeper.get().keep(this, callback);
    }

    @Override
    public void onQueryFinished(int idQuery, ResultInfo queryInfo, T data, E error) {
        ReferenceKeeper.get().forget(this);
    }
}
