package eu.hithredin.spsdk.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import eu.hithredin.spsdk.app.BaseApplication;
import eu.hithredin.spsdk.data.DeviceData;

/**
 * Created by benoit on 1/6/16.
 */
public class UtilsString {

    public static String string(int res){
        return DeviceData.ctx().getString(res);
    }

    public static NumberFormat twoDigitformatter = new DecimalFormat("#0.00");

    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }
}
