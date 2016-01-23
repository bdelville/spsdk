package eu.hithredin.spsdk.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import eu.hithredin.spsdk.R;
import eu.hithredin.spsdk.common.UtilsString;
import eu.hithredin.spsdk.data.DeviceData;

/**
 * Base Fragment that contains helpers needed to display loading, error, empty statuses
 */
public abstract class BaseLoadFragment extends BaseFragment {

    private ProgressBar spinnerCenterLoading;
    private View errorLayout;
    private View errorReloadAction;
    private TextView errorReloadText, errorTitle, errorText;

    /**
     * Show the standard error message
     */
    protected void showError() {
        showError(UtilsString.string(R.string.info_error), "", null);
    }

    protected void showIoError(View.OnClickListener reload) {
        showError(UtilsString.string(R.string.info_error_io), "", reload);
    }

    protected void showEmpty(){
        showError(UtilsString.string(R.string.info_data_empty), "", null);
    }

    protected void showError(String title, String message, View.OnClickListener reload) {
        if (errorLayout == null) {
            return;
        }
        errorLayout.setVisibility(View.VISIBLE);

        if(reload == null){
            errorReloadAction.setVisibility(View.INVISIBLE);
        } else{
            errorReloadAction.setVisibility(View.VISIBLE);
            errorReloadAction.setOnClickListener(reload);
        }
        errorTitle.setText(title);
        errorText.setText(message);
    }

    /**
     * Hide the error Message
     */
    protected void hideError() {
        if (errorLayout != null) {
            errorLayout.setVisibility(View.GONE);
        }
    }

    protected void setIsLoading(boolean status) {
        if (spinnerCenterLoading != null) {
            updateLoading(0);
            spinnerCenterLoading.setVisibility(status ? View.VISIBLE : View.GONE);
            spinnerCenterLoading.setActivated(status ? true : false);
        }
    }

    protected void updateLoading(int percent){
        spinnerCenterLoading.setProgress(percent);
    }

    protected void assignViews(View root) {
        View loader = root.findViewById(R.id.info_load_indicator);
        if(loader instanceof ProgressBar){
            spinnerCenterLoading = (ProgressBar) loader;
        }

        errorLayout = root.findViewById(R.id.info_layout);
        errorReloadAction = root.findViewById(R.id.info_reload);
        errorReloadText = (TextView) root.findViewById(R.id.info_reload_text);
        errorTitle = (TextView) root.findViewById(R.id.info_title);
        errorText = (TextView) root.findViewById(R.id.info_text);
    }

    protected void populateViews(Bundle savedInstanceState, ScreenStatus screenStatus){
        if(spinnerCenterLoading != null) {
            spinnerCenterLoading.setMax(100);
        }
    }

}
