package eu.hithredin.spsdk.image;

import android.widget.ImageView;

/**
 * Created by benoit on 1/6/16.
 */
public class ImageLoading {

    public enum ImageSdk {
        VOLLEY, UAIL
    }

    private ImageSdk sdk;
    private static ImageLoading instance;

    private ImageLoading(){

    }

    public static ImageLoading get(){
        if(instance == null){
            instance = new ImageLoading();
        }
        return instance;
    }

    public ImageLoading sdk(ImageSdk sdk){
        this.sdk = sdk;
        return this;
    }

    /**
     * Load the image to the view, and handle everything here.
     * Proxy to any loading image SDK, as customImageView are bad pattern, they are not supported
     * @param view
     * @param image
     */
    public void loadImage(ImageView view, AsyncImage image){
        //TODO

        switch (sdk){
            case UAIL:

                break;

            case VOLLEY:

                break;
        }
    }
}
