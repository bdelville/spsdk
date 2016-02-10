package eu.hithredin.spsdk.query;

/**
 * A callback proxy use to do another generic action, that the caller should not care about
 * (Like updating a DataManager, or generating global event)
 */
public abstract class ProxyRunnerQueryCallback<T,E> extends ProxyQueryCallback<T,E,T,E> {

    public ProxyRunnerQueryCallback(QueryCallback<T, E> callback) {
        super(callback);
    }

    protected abstract void run(int idQuery, ResultInfo queryInfo, T data, E error);

    @Override
    public void onQueryFinished(int idQuery, ResultInfo queryInfo, T data, E error) {
        super.onQueryFinished(idQuery, queryInfo, data, error);
        run(idQuery, queryInfo, data, error);

        if(callbackRef.get() != null){
            callbackRef.get().onQueryFinished(idQuery, queryInfo, data, error);
        }
    }
}
