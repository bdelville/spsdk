package eu.hithredin.spsdk.common.event;

import android.content.res.Configuration;

/**
 * Created by SEDONAINTRA\bdelville on 09/04/15.
 */
public class OrientationAppChanged {
    private Configuration config;

    public OrientationAppChanged(Configuration config) {
        this.config = config;
    }
}
