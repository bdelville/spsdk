package eu.hithredin.spsdk.common;

import java.lang.ref.WeakReference;
import java.util.List;

import eu.hithredin.spsdk.query.QueryCallback;

/**
 * Created by benoit on 3/30/16.
 */
public class UtilsCollection {

    public static <T> void weakAddUnique(List<WeakReference<T>> list, T object){
        for(int i=0; i< list.size(); i++){
            T qc = list.get(i).get();

            if(qc == null || qc.equals(object)){
                list.remove(list.get(i));
                i--;
                continue;
            }
        }

        list.add(new WeakReference<>(object));
    }
}
