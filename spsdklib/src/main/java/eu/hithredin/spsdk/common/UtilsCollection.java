package eu.hithredin.spsdk.common;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.hithredin.spsdk.query.QueryCallback;

/**
 * Utils to help usage of java collections
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

    public static <T> void clearDuplicate(List<T> list){
        Set<T> set = new HashSet<>(list);
        list.clear();
        list.addAll(set);
    }
}
