package eu.hithredin.spsdk.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;


import de.greenrobot.event.EventBus;
import eu.hithredin.spsdk.data.DeviceData;
import eu.hithredin.spsdk.ui.ScreenStatus;
import hugo.weaving.DebugLog;

/**
 * Base Fragment that all Fragments should extend.
 * It includes:
 * - Helper to know if this is the first time the app resumes
 * - Screen status analyser (landscape, ...)
 */
public abstract class BaseFragment extends Fragment {

private static final String LOG_TAG = BaseFragment.class.getSimpleName();

    protected boolean firstResumed = true;

    private ScreenStatus screenStatus;

    protected boolean isBusConnected = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(isBusConnected){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(isBusConnected){
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * Get infos about the actual screen state
     * @return
     */
    public ScreenStatus getScreenStatus(){
        if(screenStatus == null){
            screenStatus = getScreenStatus(DeviceData.get().getContext().getResources().getConfiguration());
        }
        return screenStatus;
    }

    /**
     * Get info about what would be the screen state with this new config
     * @param config
     * @return
     */
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
        //TODO Check to avoid over call of this
        DeviceData.get().reinit();

        if(screenStatus != (screenStatus = getScreenStatus(newConfig))) {
            runLater(new Runnable() {
                @Override
                public void run() {
                    actionOnOrientation(screenStatus);
                }
            });
        }
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
     * Remove runLater action. If action is null, remove all actions
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
     * Load and find the needed view by Ids.
     * It allows all abstracted class to load its views without access to OnViewCreated
     * @param root
     */
    protected abstract void assignViews(View root);

    /**
     * All the code to configure and fill the views with the default values
     * It allows all abstracted class to pre-fill its views without access to OnViewCreated
     * @param savedInstanceState
     * @param screenStatus
     */
    protected abstract void populateViews(Bundle savedInstanceState, ScreenStatus screenStatus);

    protected final void populateViews(Bundle savedInstanceState){
        populateViews(savedInstanceState, getScreenStatus());
    }

    /**
     * If the activity has one direction this is never called<br/>
     * Else let the fragment decides what to do: nothing, some tricks, or relayout
     * @param screenStatus
     */
    protected abstract void actionOnOrientation(ScreenStatus screenStatus);

    @Override
    /**
     * NullPointer Exception en Android 4.0.3 sur le FragmentManager ligne 1542
     */
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null) {
            outState = new Bundle();
        }
        outState.putString("NullPointer_SafeGuard", "no_crash");
        super.onSaveInstanceState(outState);
    }

    @Override
    @DebugLog
    public void onPause() {
        super.onPause();
        firstResumed = false;
        runLaterCancel(null);
    }

    @Override
    @DebugLog
    public void onResume() {
        super.onResume();
    }

}
