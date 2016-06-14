package eu.hithredin.spsdk.ui.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewParent;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import eu.hithredin.spsdk.data.DeviceData;

/**
 * Behave like a RecyclerView, but use a LinearLayout instead
 * Useful to use a Recycler adapter pattern inside a ScrollView when the cell number is limited
 * TODO handle the recycling of CellHolder and scrolling items within virtual height
 */
public class AdapterLinearLayout extends LinearLayout implements OnTouchListener, OnClickListener {

    private RecyclerAdapter adapter;
    private OnItemClickListener listener;

    private ScrollView scrollParent;
    private SparseArray<List<ReCellHolder>> recycled = new SparseArray<>();
    private List<Integer> typeActive = new ArrayList<>();

    private RecyclerView.AdapterDataObserver dataSetObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onChanged() {
            super.onChanged();
            invalidateViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            invalidateViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            invalidateViews();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            invalidateViews();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            invalidateViews();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            invalidateViews();
        }

    };

    @SuppressLint("NewApi")
    public AdapterLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AdapterLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdapterLinearLayout(Context context) {
        this(context, null);
    }

    @SuppressLint("NewApi")
    private void init() {
        //Check if the orientation was set from XML in the constructor before setting default
        if (!orientationSetted) {
            setOrientation(LinearLayout.VERTICAL);
        }

        if (DeviceData.get().getSdk() > 10) {
            setMotionEventSplittingEnabled(false);
        }
    }

    private boolean orientationSetted = false;

    public void setOrientation(int orientation) {
        orientationSetted = true;
        super.setOrientation(orientation);
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecyclerAdapter newAdapter) {
        if (adapter != null && dataSetObserver != null) {
            adapter.unregisterAdapterDataObserver(dataSetObserver);
        }

        this.adapter = newAdapter;
        adapter.registerAdapterDataObserver(dataSetObserver);
        invalidateViews();
    }

    public void invalidateView(int i) {
        removeViewAt(i);
        addView(createViewAt(i), i);
    }

    /*public void setOnScrollEndedListener(ScrollEndListener scrollEnded) {
        ScrollView sparent = getScrollParent();
        if (sparent == null || !(sparent instanceof EndScrollView)) {
            Log.w("setOnScrollEndedListener", "No ScrollView as a parent of the Layout");
            return;
        }

        ((EndScrollView) sparent).setListenerEnd(scrollEnded);
    }*/

    public void invalidateView(Object item) {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            Object realItem = adapter.getItem(i);
            if (realItem.equals(item)) {
                removeViewAt(i);
                addView(createViewAt(i), i);
                return;
            }
        }
    }

    public ScrollView getScrollParent() {
        if (scrollParent == null) {
            ViewParent parent = getParent();
            while (parent != null) {
                if (parent instanceof ScrollView) {
                    scrollParent = (ScrollView) parent;
                    break;
                }
                parent = parent.getParent();
            }
        }

        return scrollParent;
    }

    public void invalidateViews() {
        if (adapter == null) {
            return;
        }

        ScrollView sparent = getScrollParent();
        int scrollY = -1;
        if (sparent != null) {
            scrollY = sparent.getScrollY();
        }

        while (getChildCount() > 0) {
            removeViewAt(0);
        }
        for (int i = 0; i < adapter.getItemCount(); i++) {
            addView(createViewAt(i));
        }

        if (scrollY >= 0 && sparent != null) {
            // le getMax retourne une valeur bizarre... On utilise toujours le scrollY du coup
            sparent.scrollTo(getScrollX(), scrollY);
        }
    }

    // RECYCLAGE
    private View createViewAt(int i) {
        int type = adapter.getItemViewType(i);
        List<ReCellHolder> recycledView = recycled.get(type);
        ReCellHolder cellHolder = null;
        if (recycledView != null && recycledView.size() > 0) {
            cellHolder = recycledView.remove(0);
        } else {
            cellHolder = adapter.onCreateViewHolder(this, type);
        }
        //View v = adapter.getView(i, temp, this);
        adapter.bindViewHolder(cellHolder, i);

        if (listener != null) {
            if (cellHolder.getView() == null) {
                Log.e("AdapterLinearLayout", "Cas etrange, dans createViewAt, getView return null for cell=" + i);
            } else {
                cellHolder.getView().setFocusable(true);
                cellHolder.getView().setClickable(true);
                cellHolder.getView().setOnClickListener(this);
            }
        }

        if (i < typeActive.size()) {
            typeActive.set(i, type);
        } else {
            typeActive.add(type);
        }
        return cellHolder.getView();
    }

    public void removeViewAt(int i) {
        /*int type = typeActive.get(i);
        typeActive.remove(i);

        View view = getChildAt(i);
        List<ReCellHolder> recycledView = recycled.get(type);
        if (recycledView == null) {
            recycledView = new ArrayList<>();
            recycled.put(type, recycledView);
        }

        recycledView.add(view);*/

        super.removeViewAt(i);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.setFocusable(true);
            v.setClickable(true);
            v.setOnClickListener(this);
        }
    }

    public int getFirstVisiblePosition() {
        return 0;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {
        if (listener == null) {
            return;
        }

        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) == v) {
                listener.onItemClick(null, v, i, 0);
                return;
            }
        }
    }
}
