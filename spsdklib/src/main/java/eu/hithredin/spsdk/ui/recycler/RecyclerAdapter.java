package eu.hithredin.spsdk.ui.recycler;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom Generic adapter for REcyclerView.
 * Abstract the type of cells resolution, inflate layout, while keeping a dynamic management (not everything configured)
 */
public class RecyclerAdapter<T> extends RecyclerView.Adapter<ReCellHolder> {

    private static final String LOG_TAG = RecyclerAdapter.class.getSimpleName();

    /**
     * Cell builder used to override all the cell construction methods
     */
    protected RecyclerCellController<T> cellController;

    /**
     * The objects that have to be displayed in the list
     */
    protected List<T> objects;
    protected List<T> objectsWithDecors;

    public List<T> getDatas() {
        return objects;
    }


    public RecyclerAdapter() {
        this(null);
    }


    public RecyclerAdapter(List<T> data) {
        super();
        if (data == null) {
            objects = new ArrayList<>();
        } else {
            objects = new ArrayList<>(data);
        }
    }

    @Override
    /**
     * Create new views (invoked by the layout manager)
     */
    public ReCellHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layout = cellController.getLayoutId(viewType);
        View view;
        ViewDataBinding vdb = null;

        if (layout > 0) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            vdb = cellController.getViewDataBinding(viewType, inflater, viewGroup, layout);
            if (vdb != null) {
                view = vdb.getRoot();
            } else {
                view = inflater.inflate(layout, viewGroup, false);
            }
        } else {
            view = new RelativeLayout(viewGroup.getContext());
        }

        ReCellHolder holder = cellController.getHolder(viewType, view);
        holder.setViewDataBinding(vdb);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        T data = getItem(position);
        return cellController.getHolderType(position, data);
    }

    @Override
    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    public void onBindViewHolder(ReCellHolder holder, int position) {
        T data = getItem(position);
        holder.initData(data, position);
        holder.fillCell();
        cellController.onCellFilled(position, holder.getView());
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public T getItem(int position) {
        return objects.get(position);
    }

    public void setCellController(RecyclerCellController<T> cellController) {
        this.cellController = cellController;
    }

    public void addDatas(List<T> datas) {
        int newPosition = objects.size();
        objects.addAll(datas);
        notifyItemRangeInserted(newPosition, datas.size());
    }

    public void setDatasNoNotify(List<T> datas) {
        objects = datas;
    }

    public void clear() {
        int lastSize = objects.size();
        objects.clear();
        notifyItemRangeRemoved(0, lastSize);
    }

    public void showNextLoader(boolean active) {
        //todo with objectsWithDecors
    }
}
