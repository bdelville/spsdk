package eu.hithredin.spsdk.common.event;

import android.app.Fragment;

/**
 * Bus Message base class
 */
public class BaseMessage {

    public long idOrigin;

    public BaseMessage(Fragment f){
        idOrigin = f.hashCode();
    }
}
