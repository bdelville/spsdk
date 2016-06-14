package eu.hithredin.spsdk.app.manager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Tool that allow to run an action on the main thread.
 */
public class LaterRunner {

    public void runLater(Runnable action){
        runLater(action, 0);
    }

    public void runLater(Runnable action, long timer){
        if(laterRunner == null){
            laterRunner = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(msg.obj instanceof Runnable){
                        ((Runnable) msg.obj).run();
                    }
                }
            };
        }

        if(timer <= 0){
            laterRunner.post(action);
        } else{
            laterRunner.postDelayed(action, timer);
        }
    }

    /**
     * Remove runLater action. If action is null, remove all actions
     * @param action
     */
    public void runLaterCancel(Runnable action){
        if(laterRunner == null){
            return;
        }
        if(action == null){
            laterRunner.removeCallbacksAndMessages(null);
        } else{
            laterRunner.removeCallbacks(action);
        }
    }

    private Handler laterRunner;
}
