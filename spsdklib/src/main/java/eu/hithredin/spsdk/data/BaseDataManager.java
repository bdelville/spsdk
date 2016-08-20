package eu.hithredin.spsdk.data;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.zip.GZIPInputStream;

/**
 * Helper to (de)serialize any object and saved it into local memory.
 * A good pattern would be to override this class, make it a singleton, and provide access via specific data entries
 * (such as saveCart(Cart cart); )
 */
public abstract class BaseDataManager {

    private boolean isFirstLaunch = false;
    protected String lastVersion;

    /**
     * Get the version code of this app before this instance was launched
     * Useful for database version update, etc....
     * @return
     */
    public String getLastVersion() {
        return lastVersion;
    }

    protected BaseDataManager() {
        lastVersion = retrieveString("VERSION_CODE");
        if (lastVersion == null) {
            isFirstLaunch = true;
        }

        String version = DeviceData.ctx().getPackageName();
        if (!version.equals(lastVersion)) {
            saveString(DeviceData.ctx().getPackageName(), "VERSION_CODE");
        }
    }

    /**
     * Save a object to memory with a default key. It may exist only one saved instance of this class
     *
     * @param o
     */
    protected void saveSingleton(Object o) {
        save(o, o.getClass(), o.getClass().getSimpleName());
    }

    /**
     * Save a object to memory
     *
     * @param o
     * @param key
     */
    protected void save(Object o, String key) {
        SharedPreferences.Editor editor = DeviceData.get().getPreferences().edit();
        editor.putString(key, new Gson().toJson(o, o.getClass()));
        editor.apply();

        /*
        //If NO JSON BUT SERIALISATION
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("tempdata");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Save a complex templated object to memory
     *
     * @param o
     * @param typeToken
     * @param key
     */
    protected void save(Object o, Type typeToken, String key) {
        SharedPreferences.Editor editor = DeviceData.get().getPreferences().edit();
        editor.putString(key, new Gson().toJson(o, typeToken));
        editor.apply();
    }

    /**
     * Return a saved complex object from memory
     *
     * @param typeToken
     * @param key
     * @param <T>
     * @return
     */
    protected <T> T retrieve(Type typeToken, String key) {
        String data = DeviceData.get().getPreferences().getString(key, null);
        if (data != null) {
            return new Gson().fromJson(data, typeToken);
        }
        return null;
    }

    /**
     * Load data fron a raw resources file as Json
     * @param typeToken
     * @param rawId
     * @param <T>
     * @return
     */
    protected static <T> T retrieveRawJson(Type typeToken, int rawId, boolean isZip) {
        try {
            InputStream is;
            if(isZip) {
                is = new GZIPInputStream(DeviceData.ctx().getResources().openRawResource(rawId));
            } else{
                is = DeviceData.ctx().getResources().openRawResource(rawId);
            }
            return new Gson().fromJson(new InputStreamReader(is), typeToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Return a saved object with default key (only one by class)
     *
     * @param clazz
     * @param <T>
     * @return
     */
    protected <T> T retrieveSingleton(Class<T> clazz) {
        /*
        //NO JSON BUT SERIALISATION
        try{
            FileInputStream fis = new FileInputStream("tempdata");
            ObjectInputStream ois = new ObjectInputStream(fis);
            T object = (T) ois.readObject();
            ois.close();
        }
        catch (Exception ex)
        {

        }*/

        return retrieve(clazz, clazz.getSimpleName());
    }

    /**
     * Return a saved object
     *
     * @param clazz
     * @param key
     * @param <T>
     * @return
     */
    protected <T> T retrieve(Class<T> clazz, String key) {
        String data = DeviceData.get().getPreferences().getString(key, null);
        if (data != null) {
            return new Gson().fromJson(data, clazz);
        }
        return null;
    }

    /**
     * Save a simple string to memory
     *
     * @param o
     * @param key
     */
    protected void saveString(String o, String key) {
        SharedPreferences.Editor editor = DeviceData.get().getPreferences().edit();
        editor.putString(key, o);
        editor.commit();
    }

    /**
     * Retrieve a simple string from memory
     *
     * @param key
     * @return
     */
    protected String retrieveString(String key) {
        return DeviceData.get().getPreferences().getString(key, null);
    }

}
