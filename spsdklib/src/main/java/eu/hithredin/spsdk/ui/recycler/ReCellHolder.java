package eu.hithredin.spsdk.ui.recycler;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;


/**
 * Holder of data and view for a cell of recyclerView
 */
public abstract class ReCellHolder<T> extends RecyclerView.ViewHolder implements OnClickListener{
    protected Context context;
    protected T data;
    protected int position;
    protected View view;
    private Object tag;
    protected ViewDataBinding viewDataBinding;

    public void setViewDataBinding(ViewDataBinding viewDataBinding) {
        this.viewDataBinding = viewDataBinding;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public ReCellHolder(View itemView) {
        super(itemView);
        view = itemView;
        view.setOnClickListener(this);
        context = itemView.getContext();

        buildHolder(itemView);
    }

    public void initData(T data, int position){
        this.data = data;
        this.position = position;
    }

    protected abstract void buildHolder(View view);

    protected abstract void fillCell();

    public View getView() {
        return view;
    }

    protected Resources resources(){
        return view.getResources();
    }

    public T getData(){
        return data;
    }

}
