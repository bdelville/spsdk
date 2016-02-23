package eu.hithredin.spsdk.app;

import android.os.Bundle;
import android.view.View;

import eu.hithredin.spsdk.ui.ScreenStatus;
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
    protected void populateViews(Bundle savedInstanceState, ScreenStatus status) {
        super.populateViews(savedInstanceState, status);
        tabManager.populateViews(savedInstanceState, status);
    }
}
