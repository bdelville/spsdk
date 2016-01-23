package eu.hithredin.spsdk.data;

import android.content.SharedPreferences;
import android.provider.ContactsContract;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by benoit on 1/6/16.
 * Helper to (de)serialize any object and saved it into local memory.
 * A good pattern would be to override this class, make it a singleton, and provide access via specific data entries 
 * (such as saveCart(Cart cart); ) 
 */
public abstract class BaseDataManager {

    /**
     * Save a object to memory with a default key. It may exist only one saved instance of this class
     * @param o
     * @param clazz
     */
    protected void save(Object o, Class clazz) {
        save(o, clazz, clazz.getSimpleName());
    }

    /**
     * Save a object to memory
     * @param o
     * @param clazz
     * @param key
     */
    protected void save(Object o, Class clazz, String key) {
        SharedPreferences.Editor editor = DeviceData.get().getPreferences().edit();
        editor.putString(key, new Gson().toJson(o, clazz)); // TODO Why not o.getClass() instead of clazz?
        editor.apply();
    }

    /**
     * Save a complex templated object to memory
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
     * Return a saved object with default key
     * @param clazz
     * @param <T>
     * @return
     */
    protected <T> T retrieve(Class<T> clazz) {
        return retrieve(clazz, clazz.getSimpleName());
    }

    /**
     * Return a saved object
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
     * @param o
     * @param key
     */
    protected void save(String o, String key) {
        SharedPreferences.Editor editor = DeviceData.get().getPreferences().edit();
        editor.putString(key, o);
        editor.commit();
    }

    /**
     * Retrieve a simple string from memory
     * @param key
     * @return
     */
    protected String retrieve(String key) {
        return DeviceData.get().getPreferences().getString(key, null);
    }

}
