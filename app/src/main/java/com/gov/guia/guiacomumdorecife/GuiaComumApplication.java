package com.gov.guia.guiacomumdorecife;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Paula Pithon on 09/11/2017.
 */

public class GuiaComumApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Constantia.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
