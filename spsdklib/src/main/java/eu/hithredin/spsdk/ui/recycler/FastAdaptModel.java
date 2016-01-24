package eu.hithredin.spsdk.ui.recycler;

import android.view.View;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A model very easily usable by the RecyclerAdapter
 */
public abstract class FastAdaptModel implements Serializable {
    private int layoutId;
    private Runnable action;
    private Class holderClass;

    public FastAdaptModel() {
    }

    public FastAdaptModel(int layoutId, Class holderClass) {
        this.layoutId = layoutId;
        this.holderClass = holderClass;
    }

    public FastAdaptModel(int layoutId, Class holderClass, Runnable action) {
        this.layoutId = layoutId;
        this.action = action;
        this.holderClass = holderClass;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public void runAction() {
        if (action != null) {
            action.run();
        }
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public ReCellHolder<FastAdaptModel> getHolder(View itemView) {
        if (holderClass == null) {
            return null;
        }

        try {
            Constructor c = holderClass.getConstructor(View.class);
            return (ReCellHolder<FastAdaptModel>) c.newInstance(itemView);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class FastAdaptCellController<T extends FastAdaptModel> extends RecyclerCellController<T> {

        private final RecyclerAdapter<T> adapter;

        public FastAdaptCellController(RecyclerAdapter<T> adapter) {
            this.adapter = adapter;
        }

        @Override
        public int getLayoutId(int holderType) {
            return adapter.getItem(holderType).getLayoutId();
        }

        @Override
        public ReCellHolder getHolder(int holderType, View view) {
            return adapter.getItem(holderType).getHolder(view);
        }

        @Override
        public int getHolderType(int position, FastAdaptModel data) {
            return position;
        }
    }
}