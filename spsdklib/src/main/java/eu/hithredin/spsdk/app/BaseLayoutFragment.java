package eu.hithredin.spsdk.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;


/**
 * A Fragment that is directly displayed in a Activity as main fragment.
 * It is barely a container of other fragments, most of the time.
 * Used when the base activity is too complex, and here lies only the logic of putting functionnals fragments togethers
 */
public abstract class BaseLayoutFragment extends BaseLoadFragment {

    private static final String LOG_TAG = BaseLayoutFragment.class.getSimpleName();

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
     * @param fragment Fragment to add to the layoutFragment
     * @param args Arguments to give to the fragment. If null, we will provide the activity intent's extras
     * @param id_container id of the container for this fragment
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
        transaction.replace(id_container, fragment, ""+id_container);
        transaction.commit();
    }

    /**
     * When the activity is asked to display the same MainFragment: it can just update its arguments
     * @param argumentsRuntime
     */
    public void setArgumentsRuntime(Bundle argumentsRuntime) {
        getArguments().putAll(argumentsRuntime);
        populateViews(null, getScreenStatus());
    }
}
