package eu.hithredin.spsdk.app.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import eu.hithredin.spsdk.R;
import eu.hithredin.spsdk.common.UtilsOther;
import eu.hithredin.spsdk.common.UtilsString;
import eu.hithredin.spsdk.query.ResultInfo;
import hugo.weaving.DebugLog;

/**
 * Base Fragment that contains helpers needed to display loading, error, empty statuses
 */
public abstract class BaseLoadFragment extends BaseFragment {

    protected ProgressBar spinnerCenterLoading;
    protected View errorReloadAction;
    protected TextView errorReloadText;
    protected TextView errorTitle;
    protected TextView errorText;
    private int loadQueryId;

    /**
     * Show the standard error message
     */
    protected void showError() {
        showError(UtilsString.string(R.string.info_error), "", false);
    }

    protected void showIoError(boolean showReload) {
        showError(UtilsString.string(R.string.info_error_io), "", showReload);
    }

    protected void showEmpty() {
        showError(UtilsString.string(R.string.info_data_empty), "", false);
    }

    protected void showError(String title, String message, boolean showReload) {
        if (errorTitle != null) {
            errorTitle.setVisibility(View.VISIBLE);
            errorTitle.setText(title);
        }
        if (errorText != null) {
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(message);
        }

        if (errorReloadAction != null) {
            if (!showReload) {
                errorReloadAction.setVisibility(View.INVISIBLE);
            } else {
                errorReloadAction.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void setReloadAction(View.OnClickListener reload) {
        if (errorReloadAction != null) {
            errorReloadAction.setOnClickListener(reload);
        }
        //TODO errorReloadText
    }

    /**
     * Hide the error Message
     */
    protected void hideError() {
        if (errorTitle != null) {
            errorTitle.setVisibility(View.GONE);
        }
        if (errorText != null) {
            errorText.setVisibility(View.GONE);
        }
        if (errorReloadAction != null) {
            errorReloadAction.setVisibility(View.GONE);
        }
    }

    protected void setIsLoading(boolean status) {
        if (spinnerCenterLoading != null) {
            updateLoading(0);
            spinnerCenterLoading.setVisibility(status ? View.VISIBLE : View.GONE);
            spinnerCenterLoading.setActivated(status ? true : false);
        }
    }

    protected void updateLoading(int percent) {
        spinnerCenterLoading.setProgress(percent);
    }

    protected void assignViews(View root) {
        View loader = root.findViewById(R.id.info_load_indicator);
        if (loader instanceof ProgressBar) {
            spinnerCenterLoading = (ProgressBar) loader;
        }

        errorReloadAction = root.findViewById(R.id.info_reload);
        errorReloadText = (TextView) root.findViewById(R.id.info_reload_text);
        errorTitle = (TextView) root.findViewById(R.id.info_title);
        errorText = (TextView) root.findViewById(R.id.info_text);
    }

    protected void populateViews(Bundle savedInstanceState) {
        if (spinnerCenterLoading != null) {
            spinnerCenterLoading.setMax(100);
        }
    }


    protected int pageLoadCount;

    /**
     * Manage the pagination of the list
     *
     * @param isNext
     * @return the new page to load if iNext is true, the first page if it is false
     */
    protected int getPageCount(boolean isNext) {
        return isNext ? ++pageLoadCount : (pageLoadCount = 1);
    }

    /**
     * @return a random Id to prevent older queries from updating the view
     */
    protected int useQueryId() {
        //If called, it means that we start a new query to buildHolder the list
        hideError();
        setIsLoading(true);
        loadQueryId = UtilsOther.getRandomInt();
        return loadQueryId;
    }

    /**
     * MUST Be called after each request success
     *
     * @param idQuery
     * @return
     */
    protected boolean canProcess(int idQuery) {
        return getActivity() != null && idQuery == loadQueryId && !isDetached();
    }

    /**
     * A generic function to handle error and success of simple queries
     */
    @DebugLog
    public void queryFinished(ResultInfo resultInfo, Object datas) {
        if (!canProcess(resultInfo.idQuery)) {
            return;
        }
        setIsLoading(false);

        if (resultInfo.isSuccess() && datas != null) {
            onQueryResultSuccess();

        } else {
            // An error has occured
            onQueryError(resultInfo);
        }
    }

    /**
     * Handle the query result
     */
    protected void onQueryResultSuccess() {
    }

    /**
     * Handle the query error behaviour
     *
     * @param resultInfo
     */
    protected void onQueryError(ResultInfo resultInfo) {
        switch (resultInfo.codeQuery) {
            case NETWORK_ERROR:
                showIoError(true);
                break;
            default:
                showError();
                break;
        }
    }
}
