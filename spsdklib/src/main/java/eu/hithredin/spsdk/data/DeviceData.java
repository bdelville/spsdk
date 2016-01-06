package eu.hithredin.spsdk.data;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;


/**
 * Singleton that aggregate all infos that can be found about the device.
 * Helps optimizing screen, memory performance according to what's available
 * TODO best way to differenciate phone/tablet/tv/phablet/pc/etc...
 */
public class DeviceData {

    private static DeviceData instance;
    private Context defaultContext;
    private SharedPreferences preferences;
    private int deviceWidth;
    private int deviceHeight;
    private float scaleDensity;
    private int sdk;
    private MEMORY_PERF memory;

    public static boolean DEBUG;

    public int getActionBarHeight(Context ctx) {
        TypedValue tv = new TypedValue();
        if (ctx.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)){
            return TypedValue.complexToDimensionPixelSize(tv.data, ctx.getResources().getDisplayMetrics());
        }
        return (int) ctx.getResources().getDimension(eu.hithredin.spsdk.R.dimen.titlebar_height);
    }

    public float getDimen(int res) {
        return getContext().getResources().getDimension(res);
    }

    public int getColor(int res) {
        return getContext().getResources().getColor(res);
    }

    public int getNavigationBarHeight(Context ctx) {
        Resources resources = ctx.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return dipToPixels(45);
    }

    public int getStatusBarHeight(Context ctx) {
        int resourceId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return ctx.getResources().getDimensionPixelSize(resourceId);
        }
        return dipToPixels(38);
    }

    public boolean isTablet() {
        return false;
    }

    /**
     * DO_ANYTHING if even large_Heap is very large <br/>
     * LARGE if standard Heap is very good<br/>
     * MEDIUM for standard heap < 40Mb<br/>
     * VERY_LOW for standard heap < 16Mb<br/>
     */
    public static enum MEMORY_PERF {
        VERY_LOW, LOW, MEDIUM, LARGE, DO_ANYTHING
    }

    private DeviceData() {

    }

    public static DeviceData get() {
        if (instance == null) {
            instance = new DeviceData();
        }
        return instance;
    }

    /**
     *
     * @param dipValue
     * @return the pixel value for the provided dip dimension
     */
    public int dipToPixels(float dipValue) {
        return (int)(dipValue * scaleDensity);
    }

    /**
     * Check if it is the first time that the app has been launched
     * @return
     */
    public boolean isFirstLaunch(){
        boolean first = preferences.getBoolean("isFirstLaunch", true);
        if(first){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstLaunch", false);
            editor.commit();
        }
        return first;
    }

    /**
     * Must initialise at statup and/or onConfigChanged
     *
     * @param app
     */
    public void init(Application app, boolean debug) {
        DEBUG = debug;
        defaultContext = app.getApplicationContext();
        reinit();
    }

    @SuppressLint("NewApi")
    public MEMORY_PERF getRamPower(){
        if(memory == null) {
            if(sdk < 11){
                memory = MEMORY_PERF.VERY_LOW;
                return memory;
            }

            ActivityManager am = (ActivityManager) defaultContext.getSystemService(Context.ACTIVITY_SERVICE);
            int memoryClass = am.getMemoryClass();
            int memoryClassLarge = am.getLargeMemoryClass();

            if (memoryClass <= 16) {
                memory = MEMORY_PERF.VERY_LOW;
            } else if(memoryClass <= 30){
                memory = MEMORY_PERF.LOW;
            } else if(memoryClass <= 60){
                memory = MEMORY_PERF.MEDIUM;
            } else if(memoryClassLarge <= 150){
                memory = MEMORY_PERF.LARGE;
            } else{
                memory = MEMORY_PERF.DO_ANYTHING;
            }
        }

        return memory;
    }

    /**
     * Init all constant value of the device. Must be call when device changes (orientation, etc...)
     */
    public void reinit() {
        //Device dimensions
        WindowManager windowManager = (WindowManager) defaultContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);

        preferences = PreferenceManager.getDefaultSharedPreferences(defaultContext);
        deviceWidth = dm.widthPixels;
        deviceHeight = dm.heightPixels;
        scaleDensity = dm.scaledDensity;

        //Sdk version
        sdk = 14;
        try {
            sdk = android.os.Build.VERSION.SDK_INT;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Context getContext() {
        return defaultContext;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public int getDeviceWidth() {
        return deviceWidth;
    }

    public int getDeviceHeight() {
        return deviceHeight;
    }

    public float getScaleDensity() {
        return scaleDensity;
    }

    public int getSdk() {
        return sdk;
    }

    public boolean isDpiXXHigh() {
        if (scaleDensity > 2) {
            return true;
        }
        return false;
    }

    public boolean isDpiXHigh() {
        if (scaleDensity <= 2 && scaleDensity > 1.5) {
            return true;
        }
        return false;
    }

    public boolean isDpiHigh() {
        if (scaleDensity <= 1.5 && scaleDensity > 1) {
            return true;
        }
        return false;
    }

    public boolean isDpiMedium() {
        if (scaleDensity <= 1 && scaleDensity > 0.75) {
            return true;
        }
        return false;
    }

    public boolean isDpiLow() {
        if (scaleDensity <= 0.75) {
            return true;
        }
        return false;
    }

}
