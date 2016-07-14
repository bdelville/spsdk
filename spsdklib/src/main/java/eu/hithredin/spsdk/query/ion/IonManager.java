package eu.hithredin.spsdk.query.ion;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;

import java.io.InputStream;

import eu.hithredin.spsdk.data.DeviceData;
import eu.hithredin.spsdk.query.BaseRequestBuilder;
import eu.hithredin.spsdk.query.LibQueryManager;
import eu.hithredin.spsdk.query.ResultInfo;

/**
 * Created by benoit on 7/14/16.
 */
public class IonManager implements LibQueryManager {

    public static IonManager instance;

    private IonManager() {

    }

    public static IonManager get() {
        if (instance == null) {
            instance = new IonManager();
        }
        return instance;
    }

    public void launch(final BaseRequestBuilder request) {
        launch(request, true);
    }


    public void launch(final BaseRequestBuilder request, boolean isStream) {
        Builders.Any.B ion = Ion.with(DeviceData.ctx()).load(request.getUrl());

        if (isStream) {
            ion.asInputStream().withResponse().setCallback(new FutureCallback<Response<InputStream>>() {
                @Override
                public void onCompleted(Exception e, Response<InputStream> result) {
                    parseQuery(request, e, result);
                }
            });
        } else {
            ion.asString().withResponse().setCallback(new FutureCallback<Response<String>>() {
                @Override
                public void onCompleted(Exception e, Response<String> result) {
                    parseQuery(request, e, result);
                }
            });
        }
    }

    protected <U> void parseQuery(BaseRequestBuilder request, Exception e, Response<U> result) {
        ResultInfo ri = new ResultInfo();

        if (e != null) {
            if (DeviceData.DEBUG) {
                e.printStackTrace();
            }

            //TODO improve error type detection
            if (result.getHeaders() == null) {
                ri.codeQuery = (ResultInfo.CODE_QUERY.NETWORK_ERROR);
            } else {
                ri.httpCode = result.getHeaders().code();
                ri.codeQuery = (ResultInfo.CODE_QUERY.SERVER_ERROR);
            }

            request.parseError(result.getResult(), ri);

        }
        else {
            ri.codeQuery = (ResultInfo.CODE_QUERY.SUCCESS);
            request.parseResponse(result.getResult(), ri);
        }
    }
}
