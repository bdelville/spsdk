package eu.hithredin.spsdk.app;

import android.content.Intent;
import android.os.Bundle;
import eu.hithredin.spsdk.R;
import eu.hithredin.spsdk.common.Constant;
import eu.hithredin.spsdk.data.DeviceData;

import android.support.v4.app.FragmentTransaction;

/**
 * The activities does not define any view, but only the behaviour of the screen (orientation, ...)
 * Fragments layout is done within the MainContainer Fragment.
 *
 * Activity: The functionnality that the user wants. Does contains only one LayoutFragment.
 * LayoutFragment : Arrange different or one fragment according to the device state (tablet, landscape, ...)
 * Fragment: Single functionnality fragment
 *
 * Advantages of this structure:
 * - The Content fragment will be very flexible and autonomous to be used elsewhere easily later on.
 * - The Layout Fragment is kept simple, you will not lose a lot of code by recoding a new one
 * - Activity is code-free, while would we need to create another class for nothing
 * - Only one context type, the Fragment, we do not need to wonder if I need a Fragment or Activity
 */
public class MainActivity extends BaseLifeActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static void openPage(Class clazzFragment){
        openPage(clazzFragment, new Bundle());
    }

    /**
     * Open a new activity
     * @param clazzFragment
     * @param args
     */
    public static void openPage(Class clazzFragment, Bundle args){
        Intent intent = new Intent(DeviceData.ctx(), MainActivity.class);
        intent.putExtra(Constant.IntentPageFragment, clazzFragment);
        intent.getExtras().putAll(args);
        BaseApplication.getCurrentActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        //TODO Only one activity to have always on top view => Custom stack handling

        try {
            //Instanciate the LayoutFragment according to the provided Intent
            Class clazz = (Class) getIntent().getSerializableExtra(Constant.IntentPageFragment);
            BaseLayoutFragment bmf = (BaseLayoutFragment) clazz.newInstance();
            bmf.setArguments(getIntent().getExtras());
            setFragment(bmf, false);
        }
        catch (Exception ie){
            ie.printStackTrace();
        }
    }

    /**
     * Set the LayoutFragment to the activity. Can be overriden for custom animations
     * @param bmf
     * @param isOnStack
     */
    protected void setFragment(BaseLayoutFragment bmf, boolean isOnStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layout, bmf, "mainFragment");
        transaction.commit();

        //EventBus.getDefault().post(new MainFragmentChangedEvent(bmf.getClass()));
    }
}
