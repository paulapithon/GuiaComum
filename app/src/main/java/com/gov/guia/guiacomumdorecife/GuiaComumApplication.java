package com.gov.guia.guiacomumdorecife;

import android.app.Application;

import com.gov.guia.guiacomumdorecife.model.Mapa;

import java.util.HashMap;

/**
 * Created by Paula Pithon on 09/11/2017.
 */

public class GuiaComumApplication extends Application {

    private static HashMap<String, Mapa> sBotoesMapa;
    private static String sCurrentMap;

    @Override
    public void onCreate() {
        super.onCreate();

        sBotoesMapa = new HashMap<>();

    }

    public static HashMap<String, Mapa> getsBotoesMapa() {
        return sBotoesMapa;
    }

    public static String getsCurrentMap() {
        return sCurrentMap;
    }

    public static void setsCurrentMap(String sCurrentMap) {
        GuiaComumApplication.sCurrentMap = sCurrentMap;
    }
}
