package eu.hithredin.spsdk.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;


import eu.hithredin.spsdk.R;

/**
 * A Fragment that is directly displayed in a Activity as main fragment.
 * It is barely a container of other fragments, most of the time. Its only view its handle is the ActionBar
 */
public class BaseLayoutFragment extends BaseFragment {

    @Override
    protected void assignViews(View root) {
        super.assignViews(root);
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    protected void populateViews(Bundle savedInstanceState, ScreenStatus screenStatus) {
        super.populateViews(savedInstanceState, screenStatus);
    }

    @Override
    protected void actionOnOrientation(ScreenStatus screenStatus) {

    }

    /**
     *
     * @param fragment
     */
    protected void setContentFragment(Fragment fragment, Bundle args, int id_container){
        //Set Arguments
        if(args != null){
            fragment.setArguments(args);
        } else{
            fragment.setArguments(getArguments());
        }

        //Show the fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(id_container, fragment, "secondFragment");
        transaction.commit();
    }

    /**
     * When the activity is asked to display the same MainFragment: it just update its arguments
     * @param argumentsRuntime
     */
    public void setArgumentsRuntime(Bundle argumentsRuntime) {
        getArguments().putAll(argumentsRuntime);
        populateViews(null, getScreenStatus());
    }

    public boolean goBack(){
        return false;
    }
}
