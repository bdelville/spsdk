package eu.hithredin.spsdk.app;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import eu.hithredin.spsdk.BuildConfig;
import eu.hithredin.spsdk.common.UtilsOther;
import eu.hithredin.spsdk.data.DeviceData;

/**
 * Base Fragment that all Fragments must extend
 */
public abstract class BaseFragment extends Fragment {

private static final String LOG_TAG = BaseFragment.class.getSimpleName();

    protected int currentQueryId = 0;
    private static final String TAG = "BaseFragment";
    protected boolean firstResumed = true;
    private ProgressBar spinnerCenterLoading;
    private View errorLayout;
    private View errorReloadBtn;
    private TextView errorReloadText, errorTitle, errorMessage;

    public enum ScreenStatus {
        PHONE_LANDSCAPE, PHONE_PORTRAIT, TABLET_LANDSCAPE, TABLET_PORTRAIT, UNKNOWN
    }

    private ScreenStatus screenStatus;

    public ScreenStatus getScreenStatus(){
        if(screenStatus == null){
            screenStatus = getScreenStatus(DeviceData.get().getContext().getResources().getConfiguration());
        }
        return screenStatus;
    }

    public static ScreenStatus getScreenStatus(Configuration config){
        int orientation = config.orientation;

        if(DeviceData.get().isTablet()){
            if(orientation == Configuration.ORIENTATION_PORTRAIT){
                return ScreenStatus.TABLET_PORTRAIT;
            } else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                return ScreenStatus.TABLET_LANDSCAPE;
            }
        } else {
            if(orientation == Configuration.ORIENTATION_PORTRAIT){
                return ScreenStatus.PHONE_PORTRAIT;
            } else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                return ScreenStatus.PHONE_LANDSCAPE;
            }
        }
        return ScreenStatus.UNKNOWN;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //We reinit here the Device Dimension static variable, because the fragment's onConfigurationChanged is called before the Application's onConfigurationChanged
        //TODO Check that is is a Screen-related configChange
        DeviceData.get().reinit();
        screenStatus = getScreenStatus(newConfig);
        runLater(new Runnable() {
            @Override
            public void run() {
                actionOnOrientation(screenStatus);
            }
        });
    }

    protected void runLater(Runnable action){
        runLater(action, 0);
    }

    protected void runLater(Runnable action, long timer){
        if(laterRunner == null){
            laterRunner = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(msg.obj instanceof Runnable){
                        ((Runnable) msg.obj).run();
                    }
                }
            };
        }

        if(timer <= 0){
            laterRunner.post(action);
        } else{
            laterRunner.postDelayed(action, timer);
        }
    }

    /**
     * Remove runLater action. If null, remove all actions
     * @param action
     */
    protected void runLaterCancel(Runnable action){
        if(laterRunner == null){
            return;
        }
        if(action == null){
            laterRunner.removeCallbacksAndMessages(null);
        } else{
            laterRunner.removeCallbacks(action);
        }
    }

    private Handler laterRunner;

    /**
     * Show the error message
     */
    protected void showErrorMessage() {
        //showErrorMessage(Wor.ding().error.noconnection, "", null); // TODO info error en debug
    }

    protected void showErrorMessage(View.OnClickListener reload) {
        //showErrorMessage(Wor.ding().error.noconnection, "", reload); // TODO info error en debug
    }

    protected void showErrorMessage(String title, String message, View.OnClickListener reload) {
        if (errorLayout == null) {
            return;
        }
        errorLayout.setVisibility(View.VISIBLE);

        if(reload == null){
            errorReloadBtn.setVisibility(View.INVISIBLE);
        } else{
            errorReloadBtn.setVisibility(View.VISIBLE);
            errorReloadBtn.setOnClickListener(reload);
        }
        errorTitle.setText(title);
        errorMessage.setText(message);
    }

    /**
     * Hide the error Message
     */
    protected void hideErrorMessage() {
        if (errorLayout != null) {
            errorLayout.setVisibility(View.GONE);
        }
    }

    protected void setCenterWaiting(boolean status) {
        if (spinnerCenterLoading != null) {
            spinnerCenterLoading.setVisibility(status ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * All the code to find the view by Ids
     * @param root
     */
    protected void assignViews(View root) {
        /*
        spinnerCenterLoading = (ProgressBar) root.findViewById(R.id.loading_indicator_center);
        errorLayout = root.findViewById(R.id.layout_load_error);

        errorReloadBtn = root.findViewById(R.id.btn_reload);
        errorReloadText = (TextView) root.findViewById(R.id.btn_reload_text);
        errorTitle = (TextView) root.findViewById(R.id.error_title);
        errorMessage = (TextView) root.findViewById(R.id.error_message);*/
    }

    /**
     * All the code to configure and fill the views with the first values
     * @param savedInstanceState
     * @param screenStatus
     */
    protected void populateViews(Bundle savedInstanceState, ScreenStatus screenStatus){
        if(errorLayout != null && errorReloadText != null && errorTitle != null){
            try {
                //errorReloadText.setText(Wor.ding().error.retry);
                //errorTitle.setText(Wor.ding().error.noconnection);
                //errorMessage.setText(Wor.ding().temp.offline_message);
            }catch(Exception e){
                e.printStackTrace();
                Log.e("errorLayout", "Error wording not provided");
            }
        }
    }

    protected final void populateViews(Bundle savedInstanceState){
        populateViews(savedInstanceState, getScreenStatus());
    }

    /**
     * If the activity is uni-directionnal this is never called (LivedActivityPortrait ...)<br/>
     * Else let the fragment decides what to do: nothing, some tricks, or relayout
     * @param screenStatus
     */
    protected abstract void actionOnOrientation(ScreenStatus screenStatus);

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null) {
            // NullPointer Exception en Android 4.0.3 sur le FragmentManager ligne 1542
            outState = new Bundle();
        }
        outState.putString("NullPointer_SafeGuard", "no_crash");
        super.onSaveInstanceState(outState);
    }

    /**
     * @return a random Id to prevent older queries to buildHolder the view
     */
    protected int useQueryId() {
        currentQueryId = UtilsOther.getRandomInt();
        return currentQueryId;
    }

    @Override
    public void onPause() {
        super.onPause();
        firstResumed = false;
        runLaterCancel(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(BuildConfig.DEBUG){
            if(this instanceof BaseLayoutFragment){
                Log.d(LOG_TAG, "onResume Container MainFragment: " + getClass().getSimpleName());
            } else{
                Log.d(LOG_TAG, "onResume Functionnal Fragment:   " + getClass().getSimpleName());
            }
        }
    }

}
