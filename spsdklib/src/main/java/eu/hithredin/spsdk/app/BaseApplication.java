package eu.hithredin.spsdk.app;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


import java.lang.ref.WeakReference;

import de.greenrobot.event.EventBus;
import eu.hithredin.spsdk.BuildConfig;
import eu.hithredin.spsdk.app.manager.LaterRunner;
import eu.hithredin.spsdk.common.event.OrientationAppChanged;
import eu.hithredin.spsdk.data.DeviceData;
import hugo.weaving.DebugLog;

/**
 * Base class that can be overriden for the Application instance. Add useful features
 */
public abstract class BaseApplication extends Application {

    private static final String LOG_TAG = BaseApplication.class.getSimpleName();

    public static String appVersion;

    private static BaseApplication app;

    private static WeakReference<Activity> currentActivity;
    protected LaterRunner runner = new LaterRunner();

    public static Activity getCurrentActivity() {
        if(currentActivity == null){
            return null;
        }
        return currentActivity.get();
    }

    public static void setCurrentActivity(Activity currentActivity) {
        BaseApplication.currentActivity = new WeakReference<>(currentActivity);
    }

    public static BaseApplication app() {
        return app;
    }

    public LaterRunner getRunner() {
        return runner;
    }

    @Override
    @DebugLog
    public void onCreate() {
        super.onCreate();
        //Fabric.with(this, new Crashlytics());
        app = this;
        DeviceData.get().init(this);

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

            runner.runLater(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new OrientationAppChanged(newConfig));
                }
            });
        }
    }

    public static LaterRunner runner() {
        return app().getRunner();
    }
}
