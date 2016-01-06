package eu.hithredin.spsdk.app;

import android.content.Intent;
import android.os.Bundle;
import eu.hithredin.spsdk.R;
import eu.hithredin.spsdk.common.Constant;
import android.support.v4.app.FragmentTransaction;

/**
 * Activity does not define any view, but only the behaviour of the screen (orientation, ...)
 * Fragments layout is done within the MainContainer Fragment
 */
public class MainActivity extends BaseLifeActivity {

    public static void openPage(Class clazzFragment){
        openPage(clazzFragment, new Bundle());
    }

    public static void openPage(Class clazzFragment, Bundle args){
        Intent intent = new Intent();
        intent.getExtras().putAll(args);
        intent.putExtra(Constant.IntentPageFragment, clazzFragment);
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
