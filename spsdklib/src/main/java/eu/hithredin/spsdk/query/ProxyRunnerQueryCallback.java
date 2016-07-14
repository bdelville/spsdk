package eu.hithredin.spsdk.query;

/**
 * A callback proxy use to do another generic action, that the caller should not care about
 * (Like updating a DataManager, or generating global event)
 */
public abstract class ProxyRunnerQueryCallback<T,E> extends ProxyQueryCallback<T,E,T,E> {

    public ProxyRunnerQueryCallback(QueryCallback<T, E> callback) {
        super(callback);
    }

    protected abstract void run(ResultInfo queryInfo, T data, E error);

    @Override
    public void onQueryFinished(ResultInfo queryInfo, T data, E error) {
        super.onQueryFinished(queryInfo, data, error);
        run(queryInfo, data, error);

        if(callbackRef.get() != null){
            callbackRef.get().onQueryFinished(queryInfo, data, error);
        }
    }
}
