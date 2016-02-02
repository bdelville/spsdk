package eu.hithredin.spsdk.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import eu.hithredin.spsdk.R;

/**
 * Helper to know when the activity resumes from the exterior or within the app.
 * Plus some base Activity helper
 */
public class BaseLifeActivity extends AppCompatActivity {

    private static boolean RFE_paused = false, RFE_finished = false, RFE_created = false;
    protected boolean isDestroyed = false, isPaused = false;

    // Take care to call this before calling onResume()
    protected boolean isFirstResume = true;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RFE_created = true;
        BaseApplication.setCurrentActivity(this);
    }

    @Override
    public void finish() {
        super.finish();
        RFE_finished = true;
    }

    @Override
    protected void onDestroy() {
        isDestroyed = true;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        RFE_paused = true;
        isPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
        isFirstResume = false;
        BaseApplication.setCurrentActivity(this);
    }

    /**
     * Call only be called once after a resume
     * @return true if the activity is resumed from outside this app
     */
    public static boolean isResumedFromExterior() {
        boolean status = !RFE_finished && (RFE_created ^ RFE_paused);
        resetResumedFromExterior();
        return status;
    }

    protected static void resetResumedFromExterior() {
        RFE_paused = RFE_finished = RFE_created = false;
    }


    /**
     * Set the LayoutFragment to the activity. Can be overriden for custom animations
     * @param bmf
     * @param isOnStack
     */
    protected void setFragment(Fragment bmf, boolean isOnStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layout, bmf, "MainFragment");
        transaction.commit();
        //EventBus.getDefault().post(new MainFragmentChangedEvent(bmf.getClass()));
    }

}
