package eu.hithredin.spsdk.common;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import eu.hithredin.spsdk.data.DeviceData;

/**
 * Helper to measure time consumed by some process
 */
public class PerformanceLogger {

    long startTime = 0;
    private String TAG = "TIME";

    private static Map<Integer, PerformanceLogger> instances = new HashMap<Integer, PerformanceLogger>(2);

    public void removeLogger(int i) {
        instances.remove(i);
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    /**
     * Get or init a performance logger - It is then possible to log multiple thread without interference
     *
     * @param i
     * @return
     */
    public static PerformanceLogger get(int i) {
        PerformanceLogger logger = instances.get(i);

        if (logger == null) {
            logger = new PerformanceLogger();
            logger.startTime = System.nanoTime();
            instances.put(i, logger);
        }
        return logger;
    }

    /**
     * Init the timer logger
     *
     * @param message
     */
    public void initTime(String message) {
        if (DeviceData.DEBUG) {
            Log.d(TAG + "-INIT", message);
        }
        startTime = System.nanoTime();
    }

    /**
     * Log the time spent since init
     *
     * @param message
     */
    public void logTime(String message) {
        //Division augmente la visibilité mais réduit la qualite de la mesure. Pour notre niveau de performance ca n'est pas grave
        double value = ((double) System.nanoTime() - (double) startTime) / 1000000;
        if (DeviceData.DEBUG) {
            Log.d(TAG, value + " ms - " + message);
        }
    }

    /**
     * Log the time spent and reset the time
     *
     * @param message
     */
    public void logAndResetTime(String message) {
        logTime(message);
        startTime = System.nanoTime();
    }
}
