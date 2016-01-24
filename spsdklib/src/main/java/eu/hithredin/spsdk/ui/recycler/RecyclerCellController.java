package eu.hithredin.spsdk.ui.recycler;

import android.view.View;

/**
 * Created by SEDONAINTRA\bdelville on 17/03/15.
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
}
