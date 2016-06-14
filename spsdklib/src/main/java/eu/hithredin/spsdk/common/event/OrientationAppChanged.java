package eu.hithredin.spsdk.common.event;

import android.content.res.Configuration;

/**
 * App managed message that notify an orientation change
 */
public class OrientationAppChanged {
    private Configuration config;

    public OrientationAppChanged(Configuration config) {
        this.config = config;
    }
}
