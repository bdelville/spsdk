package eu.hithredin.spsdk.query.ion;

import android.util.Log;

import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import eu.hithredin.spsdk.query.ResultInfo;

/**
 * Created by benoit on 2/10/16.
 */
public class IonJsonCallback<T, E> extends IonCallback<T, E> {

    private static final String LOG_TAG = IonJsonCallback.class.getSimpleName();

    protected Type parser;
    protected Type parserError;
    protected static Gson gsonDefault = new Gson();
    protected StringPreprocessor preprocessor;
    protected boolean isStream;
    protected Gson gson;

    public IonJsonCallback gson(Gson gson){
        this.gson = gson;
        return this;
    }

    public IonJsonCallback IsStream(boolean b) {
        isStream = b;
        return this;
    }

    public IonJsonCallback Parser(Type b) {
        parser = b;
        return this;
    }

    public IonJsonCallback ParserError(Type b) {
        parserError = b;
        return this;
    }

    @Override
    public void launch(Builders.Any.B ion) {
        if (isStream) {
            ion.asInputStream().withResponse().setCallback(new FutureCallback<Response<InputStream>>() {
                @Override
                public void onCompleted(Exception e, Response<InputStream> result) {
                    parseQuery(e, result);
                }
            });
        } else {
            ion.asString().withResponse().setCallback(new FutureCallback<Response<String>>() {
                @Override
                public void onCompleted(Exception e, Response<String> result) {
                    parseQuery(e, result);
                }
            });
        }
    }

    protected Gson gson(){
        if(gson == null){
            return gsonDefault;
        }
        return gson;
    }

    protected <U> void parseQuery(Exception e, Response<U> result) {
        ResultInfo ri = getResultInfo(result);
        E errordata = null;
        T data = null;

        if (e != null) {
            e.printStackTrace();

            try {
                errordata = parseData(parserError, result.getResult());
                ri.setCodeQuery(ResultInfo.CODE_QUERY.SERVER_ERROR);
            } catch (Exception ex) {
                ri.setCodeQuery(ResultInfo.CODE_QUERY.NETWORK_ERROR);
                ex.printStackTrace();
            }
        } else if(result.getResult() != null){
            data = parseData(parser, result.getResult());
            ri.setCodeQuery(ResultInfo.CODE_QUERY.SUCCESS);
        }

        sendCallback(ri, data, errordata);
    }

    public interface StringPreprocessor {
        public String preprocess(String s);
    }

    public void setPreprocessor(StringPreprocessor preprocessor) {
        this.preprocessor = preprocessor;
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
                    dataParsed = gson().fromJson(s, type);
                } else if (data instanceof InputStream) {
                    if (preprocessor != null) {
                        Log.w(LOG_TAG, "A StringProcessor is defined, but not used as you use Streams");
                    }
                    gson().fromJson(new InputStreamReader((InputStream) data), type);
                }
            }
        }
        return dataParsed;
    }

}
