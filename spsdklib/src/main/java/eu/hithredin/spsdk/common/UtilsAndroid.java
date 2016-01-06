package eu.hithredin.spsdk.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Created by benoit on 1/6/16.
 */
public class UtilsAndroid {


    public static ApplicationInfo findInfosApplication(Context ctx, String packageName){
        PackageManager pm = ctx.getPackageManager();
        try{
            return pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).applicationInfo;
        }catch (PackageManager.NameNotFoundException e) {

        }
        return null;
    }

    public static String getAppVersion(Context ctx, String packageName) {
        String version = "0";
        try {
            PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
            version =  pInfo.versionName +"."+pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e1) {

        }
        return version;
    }

    public static void installOrOpenApp(Context ctx, String packageName){
        PackageManager pm = ctx.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
            ctx.startActivity(launchIntent);
        }
        catch (PackageManager.NameNotFoundException e) {
            try {
                ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
            }
        }
    }

    public static void openMyMarketPage(Context ctx) {
        try {
            ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + ctx.getPackageName())));
        } catch (android.content.ActivityNotFoundException anfe) {
            ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + ctx.getPackageName())));
        }
    }



}
