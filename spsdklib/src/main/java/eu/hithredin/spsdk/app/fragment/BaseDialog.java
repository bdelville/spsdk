package eu.hithredin.spsdk.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;

/**
 * Base Fragment that all Fragments should extend.
 * It includes:
 * - Helper to know if this is the first time the app resumes
 * - Screen status analyser (landscape, ...)
 */
public abstract class BaseDialog extends DialogFragment {

    private static final String LOG_TAG = DialogFragment.class.getSimpleName();

    protected boolean firstResumed = true;

    protected boolean isBusConnected = false;
    protected boolean isBusAutoDisconnected = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (isBusConnected && (isBusAutoDisconnected || firstResumed)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (isBusConnected && isBusAutoDisconnected) {
            EventBus.getDefault().unregister(this);
        }
    }


    /**
     * Load and find the needed view by Ids.
     * It allows all abstracted class to load its views without access to OnViewCreated
     *
     * @param root
     */
    protected void assignViews(View root) {

    }

    /**
     * All the code to configure and fill the views with the default values
     * It allows all abstracted class to pre-fill its views without access to OnViewCreated
     *
     * @param savedInstanceState
     */
    protected void populateViews(Bundle savedInstanceState) {

    }


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
    }

    @Override
    @DebugLog
    public void onResume() {
        super.onResume();
    }

}
