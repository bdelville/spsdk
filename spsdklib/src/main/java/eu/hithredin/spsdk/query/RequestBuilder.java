package eu.hithredin.spsdk.query;

import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

/**
 * Created by benoit on 1/8/16.
 * Base class to build queries, handle callback, parsing, timeout, instance cache (if use it)
 *
 * @param <T> Succes object type
 * @param <E> Error Object type
 */
public abstract class RequestBuilder<T,E> extends SimpleRequestBuilder<T,E>{

    private static final String LOG_TAG = RequestBuilder.class.getSimpleName();
    
    protected Object parser;
    protected Object parserError;
    protected static Gson gson;


    /**
     * Override to use a specific Gson (de)serializer
     */
    public static void setGson(Gson g) {
        gson = g;
    }



    /**
     * Parser of success
     *
     * @param type
     * @return
     */
    public RequestBuilder parserJson(Type type) {
        this.parser = type;
        return this;
    }

    /**
     * Parser of error
     *
     * @param typeError
     * @return
     */
    public RequestBuilder parserErrorJson(Type typeError) {
        this.parserError = typeError;
        return this;
    }

}
