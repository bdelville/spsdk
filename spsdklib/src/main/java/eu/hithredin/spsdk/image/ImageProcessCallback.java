package eu.hithredin.spsdk.image;

import android.graphics.Bitmap;

/**
 * Created by benoit on 1/8/16.
 */
public interface ImageProcessCallback {

    /**
     * Called when the image is loaded from network before beeing put into cache neither used
     * @param bmp
     * @return If not null, the return value will be the image used by the sdk to display and put to cache
     */
    Bitmap imageLoaded(Bitmap bmp);

    /**
     * Called when the image is loaded from network or cache but not used
     * @param bmp
     * @return If not null, the return value will be the image used by the sdk to display
     */
    Bitmap imagePrepared(Bitmap bmp);
}
