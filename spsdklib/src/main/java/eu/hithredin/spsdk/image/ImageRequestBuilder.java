package eu.hithredin.spsdk.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import eu.hithredin.spsdk.query.SimpleRequestBuilder;

/**
 * Created by benoit on 1/8/16.
 */
public class ImageRequestBuilder extends SimpleRequestBuilder<Bitmap, String> {
    protected AsyncImage image;
    protected ImageView view;
    protected ImageProcessCallback processCallback;

    public enum ImageSdk {
        VOLLEY, UAIL
    }

    private static ImageSdk sdk;

    /**
     * Must be called at application startup
     */
    public static void init(){
        sdk = ImageSdk.UAIL;
        //TODO select image sdk according to available sdk in libraries (classloader)
    }

    public ImageRequestBuilder processCallback(ImageProcessCallback ipc){
        processCallback = ipc;
        return this;
    }

    /**
     * Bypass the creation of ImageRequestBuilderObject if no need of callback
     * @param view
     * @param image
     */
    public static void loadImage(ImageView view, AsyncImage image){
        //TODO Bypass creation of requestBuilder
        ImageRequestBuilder irb = new ImageRequestBuilder();
        irb.view(view).image(image);
        irb.loadAsync();
    }

    public ImageRequestBuilder image(AsyncImage image){
        this.image = image;
        return this;
    }

    public ImageRequestBuilder view (ImageView view){
        this.view = view;
        return this;
    }

    @Override
    public void loadAsync() {

        switch (sdk){
            case UAIL:
                //TODO
                break;

            case VOLLEY:
                //TODO
                break;
        }
    }
}
