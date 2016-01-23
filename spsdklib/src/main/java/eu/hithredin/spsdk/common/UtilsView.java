package eu.hithredin.spsdk.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;

import eu.hithredin.spsdk.data.DeviceData;

/**
 * Created by benoit on 1/6/16.
 */
public class UtilsView {

    /**
     * Change selection status to the inverse boolean value
     * @param layout
     * @param newEnabledChildIndex
     */
    public static void toggleSelected(ViewGroup layout, int newEnabledChildIndex){
        for(int i=0; i<layout.getChildCount(); i++){
            (layout.getChildAt(i)).setSelected(i == newEnabledChildIndex);
        }
    }

    @SuppressLint("NewApi")
    public static void removeGlobalOnLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener l){
        ViewTreeObserver vto = v.getViewTreeObserver();

        if(DeviceData.get().getSdk() < 16){
            vto.removeGlobalOnLayoutListener(l);
        } else{
            vto.removeOnGlobalLayoutListener(l);
        }
    }

    /**
     *
     * @param name
     * @return the resource string id corresponding to the provided string (R.string.*)
     */
    public static int getResString(String name){
        return DeviceData.ctx().getResources().getIdentifier(name, "string", DeviceData.ctx().getPackageName());
    }

    /**
     *
     * @param name
     * @return the resource id corresponding to the string (R.id.*)
     */
    public static int getResId(String name){
        return DeviceData.ctx().getResources().getIdentifier(name, "id", DeviceData.ctx().getPackageName());
    }

    /**
     *
     * @param view
     * @param status
     */
    public static void toggleEnabled(View view, boolean status){
        view.setEnabled(status);

        if(view instanceof ViewGroup){
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++){
                toggleEnabled(group.getChildAt(i), status);
            }
        }
    }

    /**
     * Be sure that the tag of the view is of a certain Class, help analysing view's tag
     * @param v
     * @param clazz
     * @return
     */
    public static Object findTagOfType(View v, Class clazz){
        ViewParent parent = v.getParent();

        while(parent != null){
            if(parent instanceof View){
                View parentView = (View) parent;
                Object tag = parentView.getTag();
                if(tag != null && tag.getClass() == clazz){
                    return tag;
                }
                parent = parentView.getParent();
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * Shortcut to find a view by string id
     * @param view
     * @param idString
     * @return
     */
    public static View findView(View view, String idString) {
        int id = getResString(idString);
        return view.findViewById(id);
    }

}
