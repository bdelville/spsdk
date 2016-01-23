package eu.hithredin.spsdk.common;

import eu.hithredin.spsdk.app.BaseApplication;
import eu.hithredin.spsdk.data.DeviceData;

/**
 * Created by benoit on 1/6/16.
 */
public class UtilsString {

    public static String string(int res){
        return DeviceData.ctx().getString(res);
    }
}
