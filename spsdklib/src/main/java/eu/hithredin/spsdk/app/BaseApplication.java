package eu.hithredin.spsdk.app;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import java.lang.ref.WeakReference;

import de.greenrobot.event.EventBus;
import eu.hithredin.spsdk.BuildConfig;
import eu.hithredin.spsdk.common.event.OrientationAppChanged;
import eu.hithredin.spsdk.data.DeviceData;

/**
 * Created by bdelville on 10/07/2014.
 * Base class that can be overriden for the app. Always use it
 */
public abstract class BaseApplication extends Application {

    private static final String LOG_TAG = BaseApplication.class.getSimpleName();

    public static String appVersion;

    private static BaseApplication app;

    private static WeakReference<Activity> currentActivity;

    public static Activity getCurrentActivity() {
        if(currentActivity == null){
            return null;
        }
        return currentActivity.get();
    }

    public static void setCurrentActivity(Activity currentActivity) {
        BaseApplication.currentActivity = new WeakReference<>(currentActivity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        app = this;
        DeviceData.get().init(this, BuildConfig.DEBUG);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch(Exception e) {
            e.printStackTrace();
        }

        lastOrientation = getResources().getConfiguration().orientation;
    }

    private int lastOrientation;

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(LOG_TAG, "onConfigurationChanged");


        if(lastOrientation != newConfig.orientation){
            DeviceData.get().reinit();
            lastOrientation = newConfig.orientation;

            runLater(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new OrientationAppChanged(newConfig));
                }
            });
        }
    }

    public static void runLater(Runnable action){
        runLater(action, 0);
    }

    /**
     * Run on the UI Thread later on
     * @param action
     * @param timer
     */
    public static void runLater(Runnable action, long timer){
        if(app == null){
            return;
        }
        if(timer <= 0){
            app.laterRunner.post(action);
        } else{
            app.laterRunner.postDelayed(action, timer);
        }
    }

    protected Handler laterRunner = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.obj instanceof Runnable){
                ((Runnable) msg.obj).run();
            }
        }
    };
}
