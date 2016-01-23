package eu.hithredin.spsdk.common.trick;

import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Singleton that keep a reference against the GC even if it is not referenced anymore
 */
public class ReferenceKeeper {

    private static final int MAX_KEPT_OBJECT = 15;

    private static ReferenceKeeper instance;
    private MaxSizeHashMap<KeepedReference, WeakReference> references;

    private ReferenceKeeper() {
        references = new MaxSizeHashMap<KeepedReference, WeakReference>(MAX_KEPT_OBJECT);
    }

    public static ReferenceKeeper get() {
        if (instance == null) {
            instance = new ReferenceKeeper();
        }
        return instance;
    }

    /**
     * Maintain a loosely coupled life link between lifeReference and object. When the lifeReference is deleted by the GC, the object reference may be deleted too <br/>
     * Useful to maintain a temporary callback without specific UI code
     *
     * @param object Object that is kept
     * @param lifeReference reference whoe life dictate teh life of object
     */
    public void keep(KeepedReference object, Object lifeReference) {
        references.put(object, new WeakReference(lifeReference));
    }

    /**
     * Keep a reference to the object
     * @param object
     */
    public void keep(KeepedReference object) {
        references.put(object, null);
    }

    public void forget(KeepedReference object) {
        references.remove(object);
    }

    public void parseForgettable() {
        for (Map.Entry<KeepedReference, WeakReference> entry : references.entrySet()) {
            if (entry.getValue() != null && entry.getValue().get() == null) {
                references.remove(entry);
            }
        }
    }

    public interface KeepedReference {
    }

    private class MaxSizeHashMap<K, V> extends LinkedHashMap<K, V> {
        private final int maxSize;

        public MaxSizeHashMap(int maxSize) {
            this.maxSize = maxSize;
        }

        @Override
        protected boolean removeEldestEntry(Entry<K, V> eldest) {
            if (size() > maxSize) {
                parseForgettable();
                return true;
            }
            return false;
        }
    }
}
