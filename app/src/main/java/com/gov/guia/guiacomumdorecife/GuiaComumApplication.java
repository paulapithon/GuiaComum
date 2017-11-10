package com.gov.guia.guiacomumdorecife;

import android.app.Application;

import com.gov.guia.guiacomumdorecife.model.BtnMapa;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Paula Pithon on 09/11/2017.
 */

public class GuiaComumApplication extends Application {

    private static HashMap<String, BtnMapa> sBotoesMapa;

    @Override
    public void onCreate() {
        super.onCreate();

        sBotoesMapa = new HashMap<>();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Constantia.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static HashMap<String, BtnMapa> getsBotoesMapa() {
        return sBotoesMapa;
    }

    public static void addToHashMap (String index, BtnMapa mapa) {
        sBotoesMapa.put(index, mapa);
    }
}
