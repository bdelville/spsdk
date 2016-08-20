package eu.hithredin.spsdk.app.fragment;

import android.os.Bundle;
import android.view.View;


import eu.hithredin.spsdk.ui.layout.TabManager;

/**
 * Created by benoit on 2/2/16.
 */
public abstract class BaseTabFragment extends BaseLayoutFragment {
    protected TabManager tabManager;

    protected abstract TabManager buildTabManager();

    @Override
    protected void assignViews(View root) {
        super.assignViews(root);
        tabManager = buildTabManager();
        tabManager.assignViews(root);
    }


    @Override
    protected void populateViews(Bundle savedInstanceState) {
        super.populateViews(savedInstanceState);
        tabManager.populateViews(savedInstanceState);
    }
}
