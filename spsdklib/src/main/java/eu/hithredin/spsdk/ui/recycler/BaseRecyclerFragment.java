package eu.hithredin.spsdk.ui.recycler;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eu.hithredin.spsdk.R;
import eu.hithredin.spsdk.app.fragment.BaseLoadFragment;

import eu.hithredin.spsdk.query.ResultInfo;
import hugo.weaving.DebugLog;

/**
 * Functionnal Fragment based on a RecyclerView
 * Help to query data, used the RecyclerView, Manage error/reload
 */
public abstract class BaseRecyclerFragment<T> extends BaseLoadFragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private AdapterLinearLayout linearLayout;

    private RecyclerView.LayoutManager layoutManager;
    protected RecyclerAdapter<T> adapter;
    protected SwipeRefreshLayout swipeRefreshLayout;

    /**
     * Define you cells datas, layout, etc...
     * @return
     */
    protected abstract RecyclerCellController buildCellController();


    protected void assignViews(View root){
        super.assignViews(root);

        View view = root.findViewById(R.id.recyclerview);
        if(view instanceof AdapterLinearLayout) {
            linearLayout = (AdapterLinearLayout) view;
        }
        else {
            recyclerView = (RecyclerView) view;
        }
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.activity_main_swipe_refresh_layout);
    }


    protected void populateViews(Bundle savedInstanceState){
        super.populateViews(savedInstanceState);

        adapter = buildAdapter();
        adapter.setCellController(buildCellController());

        if(recyclerView != null) {
            layoutManager = buildRecyclerLayoutManager();
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
        if(linearLayout != null){
            //layoutManager can only be a vertical LinearLayout Manager
            linearLayout.setAdapter(adapter);
        }

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(this);;
        }

        setReloadAction(clickReload);
    }


    /**
     * Override for a specific adapter
     * @return
     */
    protected RecyclerAdapter<T> buildAdapter() {
        return new RecyclerAdapter<>();
    }

    /**
     * Use your own layout, or fragment_recycler_refresh_base to enable SwipeRefresh
     * @return the layout id for this fragment
     */
    protected int fragmentLayout() {
        return R.layout.fragment_recycler_base;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(fragmentLayout(), container, false);
        assignViews(root);
        populateViews(savedInstanceState);

        cleanList();
        loadListQuery();
        return root;
    }

    /**
     * Override to force a LayoutManager for the recyclerView

     */
    protected RecyclerView.LayoutManager buildRecyclerLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override
    public void onRefresh() {
        cleanList();
        loadListQuery();
    }

    protected void cleanList(){
        adapter.clear();
        pageLoadCount = 0;
        setIsLoading(false);
    }

    private View.OnClickListener clickReload = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Restart from scratch
            adapter.clear();
            pageLoadCount = 0;
            loadListQuery();
        }
    };


    /**
     * Call when you can start/continue the data query
     */
    protected abstract void loadListQuery();

    /**
     * A generic function to handle error and fill the recyclerview
     */
    @DebugLog
    public void queryListFinished(int idQuery, ResultInfo resultInfo, List<T> datas) {
        if (!canProcess(idQuery)) {
            return;
        }
        setIsLoading(false);

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }

        if (resultInfo.isSuccess() && datas != null) {
            if (datas.size() == 0) {
                // List empty
                if (!onQueryResultEmpty(adapter.getItemCount() == 0)) {
                    adapter.showNextLoader(false);
                }
            }
            else {
                //List is ok
                onListQueryResultSuccess(datas);
            }

        } else {
            onListQueryError(resultInfo);
        }
    }

    protected void onListQueryResultSuccess(List<T> datas) {
        adapter.addDatas(datas);
    }

    protected void onListQueryError(ResultInfo resultInfo) {
        adapter.showNextLoader(false);
        if (adapter.getItemCount() == 0) {
            super.onQueryError(resultInfo);
        }
    }


    /**
     * @return true to prevent default empty behaviour
     */
    protected boolean onQueryResultEmpty(boolean listIsEmpty) {
        if (listIsEmpty) {
            showEmpty();
        }
        return false;
    }
}
