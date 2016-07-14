package eu.hithredin.spsdk.query;

import android.util.Log;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import eu.hithredin.spsdk.data.DeviceData;

/**
 * Created by benoit on 1/8/16.
 * Base class to build queries, handle callback, parsing, timeout, instance cache (if use it)
 *
 * @param <T> Succes object type
 * @param <E> Error Object type
 */
public class JsonRequestBuilder<T,E> extends BaseRequestBuilder<T,E> {

    private static final String LOG_TAG = JsonRequestBuilder.class.getSimpleName();
    
    protected Type parser;
    protected Type parserError;
    protected static Gson gson;
    protected static Gson gsonDefault = new Gson();
    protected StringPreprocessor preprocessor;

    public JsonRequestBuilder gson(Gson gson){
        this.gson = gson;
        return this;
    }

    /**
     * Parser of success
     *
     * @param type
     * @return
     */
    public JsonRequestBuilder parserJson(Type type) {
        this.parser = type;
        return this;
    }

    /**
     * Parser of error
     *
     * @param typeError
     * @return
     */
    public JsonRequestBuilder parserErrorJson(Type typeError) {
        this.parserError = typeError;
        return this;
    }

    public interface StringPreprocessor {
        String preprocess(String s);
    }

    public void setPreprocessor(StringPreprocessor preprocessor) {
        this.preprocessor = preprocessor;
    }


    protected Gson getGson(){
        if(gson == null){
            return gsonDefault;
        }
        return gson;
    }

    @Override
    public void parseResponse(Object data, ResultInfo ri) {
        T result = null;

        try {
            if(data != null){
                result = parseData(parser, data);
            }
        } catch (Exception ex){
            if(DeviceData.DEBUG){
                ex.printStackTrace();
            }
            ri.codeQuery = ResultInfo.CODE_QUERY.SERVER_ERROR;
        }
        sendCallback(ri, result, null);
    }

    @Override
    public void parseError(Object data, ResultInfo ri) {
        E errorData = null;

        try {
            if(data != null){
                errorData = parseData(parserError,data);
            }
        } catch (Exception ex) {
            ri.codeQuery = (ResultInfo.CODE_QUERY.SERVER_ERROR);
            if(DeviceData.DEBUG){
                ex.printStackTrace();
            }
        }

        sendCallback(ri, null, errorData);
    }

    /**
     * @param dataParser
     * @param data
     * @param <U>        T or E depending of the data to be parsed
     * @return
     */
    protected <U> U parseData(Object dataParser, Object data) {
        U dataParsed = null;

        if (dataParser != null) {
            if (dataParser instanceof Type) {
                //Json parsing
                Type type = (Type) dataParser;

                if (data != null && data instanceof String) {
                    String s = (String)data;
                    if (preprocessor != null) {
                        s = preprocessor.preprocess(s);
                    }
                    dataParsed = getGson().fromJson(s, type);
                }
                else if (data instanceof InputStream) {
                    if (preprocessor != null) {
                        Log.w(LOG_TAG, "A StringProcessor is defined, but not used as you use Streams");
                    }
                    dataParsed = getGson().fromJson(new InputStreamReader((InputStream) data), type);
                }
            }
        }
        return dataParsed;
    }

}
