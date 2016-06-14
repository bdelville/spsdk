package eu.hithredin.spsdk.ui.recycler;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Controller used by a view to handle the cell of the recyclerView
 */
public abstract class RecyclerCellController<T> {

    /**
     *
     * @param holderType
     * @return the id of the layout for this type
     */
    abstract public int getLayoutId(int holderType);

    /**
     *
     * @param holderType
     * @param view
     * @return a new CellHolder instance for this type
     */
    abstract public ReCellHolder getHolder(int holderType, View view);

    /**
     * By default only one cell type
     * @param position
     * @param data
     * @return Type of CellHolder to use, most important
     */
    public int getHolderType(int position, T data){
        return 0;
    }

    /**
     * Invoked when the cell is filled with datas
     * @param position
     * @param cell
     */
    public void onCellFilled(int position, final View cell) {

    }

    /**
     * Don't forget to setup the databinding inside fillCell
     * @param holderType
     * @param inflater
     * @param root
     * @param layout
     * @return null is not DataBinding
     */
    public ViewDataBinding getViewDataBinding(int holderType, LayoutInflater inflater, @Nullable ViewGroup root, int layout){
        //MyListItemBinding.inflate(inflater, layout, viewGroup, false);
        return null;
    }

    /**
     * Set the dataBinding here avoid the creation of the CellHolder
     * @param vdb
     * @param data
     */
    public void setViewDataBinding(ViewDataBinding vdb, T data){

    }
}
