package eu.hithredin.spsdk.app;

import eu.hithredin.spsdk.common.BeanCacheMap;

/**
 * Created by benoit on 7/18/16.
 */
public class PreloadManager {
    private static PreloadManager instance = new PreloadManager();

    public static PreloadManager get() {
        return instance;
    }

    private PreloadManager() {
    }

    private BeanCacheMap<Preloader> preloaded = new BeanCacheMap(3);

    /**
     * Container to describe why we try to preload, use for stats
     */
    public static class PreloadReason{

    }

    /**
     * To be associated with teh activity
     */
    public static class Preloader{

        public Preloader(PreloadReason reason) {

        }

        public void load() {

        }

        public void start() {

        }
    }

    public void preload(PreloadReason reason, int paramHash, Class activity){
        //1 - Check if a similar preload has been done with same parameter
        Preloader loader = preloaded.get(paramHash);
        if(loader == null){
            loader = new Preloader(reason);
            preloaded.put(paramHash, loader);
        }

        //2 - Ask the activity to create the fragment
        loader.load();
    }

    public void load(int paramHash, Class activity){
        //1- Check if a similar preload has been done with same parameters
        Preloader loader = preloaded.get(paramHash);

        //2- If yes, provide the created Fragment to the activity, with data loaded
        // => Only view creation needed
        // => Save probability success stats by PreloadReason
        if(loader == null){
            loader = new Preloader();
        } else{
            loader.saveStats();
        }

        loader.start();
    }


}
