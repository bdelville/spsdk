package eu.hithredin.spsdk.app;

import android.content.Intent;
import android.os.Bundle;

import eu.hithredin.spsdk.R;
import eu.hithredin.spsdk.app.fragment.BaseLayoutFragment;
import eu.hithredin.spsdk.common.Constant;
import eu.hithredin.spsdk.data.DeviceData;

/**
 * This activity does not define any view, but only the behaviour of the screen (orientation, ...)
 * Fragments layout is done within the MainContainer Fragment.
 * <p>
 * Activity: The use-case that the user wants
 * LayoutFragment : Arrange different or one fragment according to the device state (tablet, landscape, ...)
 * Fragment: Single functionnality fragment
 * <p>
 * Advantages of this structure:
 * - The Content fragment will be very flexible and autonomous to be used elsewhere easily later on.
 * - The Layout Fragment is kept simple, you will not lose a lot of code by recoding a new one for a different form factor
 * - Only one context type, the Fragment, we do not need to wonder if I need a Fragment or Activity
 */
public class MainActivity extends BaseLifeActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static void openPage(Class clazzFragment) {
        openPage(clazzFragment, new Bundle());
    }

    /**
     * Open a new activity
     *
     * @param clazzFragment
     * @param args
     */
    public static void openPage(Class clazzFragment, Bundle args) {
        Intent intent = new Intent(DeviceData.ctx(), MainActivity.class);
        intent.putExtra(Constant.IntentPageFragment, clazzFragment);
        intent.getExtras().putAll(args);
        BaseApplication.getCurrentActivity().startActivity(intent);
    }

    protected int getLayoutId() {
        return R.layout.activity_simple;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        //TODO Only one activity to have always on top view => Custom stack handling

        try {
            //Instanciate the LayoutFragment according to the provided Intent
            Class clazz = (Class) getIntent().getSerializableExtra(Constant.IntentPageFragment);
            BaseLayoutFragment bmf = (BaseLayoutFragment) clazz.newInstance();
            bmf.setArguments(getIntent().getExtras());
            setFragment(bmf, false);
        } catch (Exception ie) {
            ie.printStackTrace();
        }
    }

}
