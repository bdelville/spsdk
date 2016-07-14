package eu.hithredin.spsdk.query;

/**
 * If no Error to parse, use a Void type
 * @param <T> Data type response
 * @param <E> Data type error
 */
public interface QueryCallback <T,E> {

    void onQueryFinished(ResultInfo queryInfo, T data, E error);
}
