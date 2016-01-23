package eu.hithredin.spsdk.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import eu.hithredin.spsdk.data.DeviceData;

/**
 * Created by benoit on 1/6/16.
 */
public class UtilsAndroid {


    private static final String LOG_TAG = UtilsAndroid.class.getSimpleName();

    /**
     * Shortcut to get ApplicationInfo for a given package
     * @param packageName
     * @return
     */
    public static ApplicationInfo getAppInfos(String packageName){
        PackageManager pm = DeviceData.ctx().getPackageManager();
        try{
            return pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).applicationInfo;
        }catch (PackageManager.NameNotFoundException e) {
            Log.d(LOG_TAG, "getAppInfos, the application has not been found: " + packageName);
        }
        return null;
    }

    /**
     * Get the application version for this package
     * @param packageName
     * @return
     */
    public static String getAppVersion(String packageName) {
        String version = null;
        try {
            PackageInfo pInfo = DeviceData.ctx().getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
            version =  pInfo.versionName +"."+pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e1) {
            Log.d(LOG_TAG, "getAppVersion, the application has not been found: " + packageName);
        }
        return version;
    }

    /**
     * Launch an application or redirect to the market page if not installed
     * @param packageName
     */
    public static void installOrOpenApp(String packageName){
        PackageManager pm = DeviceData.ctx().getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
            DeviceData.ctx().startActivity(launchIntent);
        }
        catch (PackageManager.NameNotFoundException e) {
            openMarketPage(packageName);
        }
    }

    /**
     * Open the play store to this application page
     */
    public static void openMyMarketPage() {
        openMarketPage(DeviceData.ctx().getPackageName());
    }

    public static void openMarketPage(String packageName){
        try {
            DeviceData.ctx().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            DeviceData.ctx().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
        }
    }




}
